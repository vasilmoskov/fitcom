package bg.softuni.fitcom.services.impl;

import bg.softuni.fitcom.exceptions.ResourceNotFoundException;
import bg.softuni.fitcom.models.entities.BodyPartEntity;
import bg.softuni.fitcom.models.entities.CommentEntity;
import bg.softuni.fitcom.models.entities.ExerciseEntity;
import bg.softuni.fitcom.models.entities.GoalEntity;
import bg.softuni.fitcom.models.entities.RoleEntity;
import bg.softuni.fitcom.models.entities.TrainingProgramEntity;
import bg.softuni.fitcom.models.entities.UserEntity;
import bg.softuni.fitcom.models.enums.BodyPartEnum;
import bg.softuni.fitcom.models.enums.RoleEnum;
import bg.softuni.fitcom.models.service.CommentAddServiceModel;
import bg.softuni.fitcom.models.service.TrainingProgramServiceModel;
import bg.softuni.fitcom.models.view.CommentViewModel;
import bg.softuni.fitcom.models.view.ExerciseOverviewViewModel;
import bg.softuni.fitcom.models.view.TrainingProgramDetailsViewModel;
import bg.softuni.fitcom.models.view.TrainingProgramsOverviewViewModel;
import bg.softuni.fitcom.repositories.BodyPartRepository;
import bg.softuni.fitcom.repositories.CommentRepository;
import bg.softuni.fitcom.repositories.GoalRepository;
import bg.softuni.fitcom.repositories.TrainingProgramRepository;
import bg.softuni.fitcom.repositories.UserRepository;
import bg.softuni.fitcom.services.TrainingProgramService;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class TrainingProgramServiceImpl implements TrainingProgramService {
    private final TrainingProgramRepository trainingProgramRepository;
    private final CommentRepository commentRepository;
    private final ModelMapper modelMapper;
    private final UserRepository userRepository;
    private final GoalRepository goalRepository;
    private final BodyPartRepository bodyPartRepository;

    public TrainingProgramServiceImpl(TrainingProgramRepository trainingProgramRepository,
                                      CommentRepository commentRepository, UserRepository userRepository,
                                      GoalRepository goalRepository, BodyPartRepository bodyPartRepository,
                                      ModelMapper modelMapper) {
        this.trainingProgramRepository = trainingProgramRepository;
        this.commentRepository = commentRepository;
        this.modelMapper = modelMapper;
        this.userRepository = userRepository;
        this.goalRepository = goalRepository;
        this.bodyPartRepository = bodyPartRepository;
    }

    @Transactional
    @Override
    public List<TrainingProgramsOverviewViewModel> getTrainingPrograms(String title,
                                                                       String bodyPart,
                                                                       Integer pageNo,
                                                                       Integer pageSize,
                                                                       String sortBy) {

        if (pageNo < 0) {
            return Collections.emptyList();
        }

        Pageable paging = PageRequest.of(pageNo, pageSize, Sort.by(sortBy));

        BodyPartEnum bodyPartEnum = bodyPart != null && !bodyPart.equals("") ? BodyPartEnum.valueOf(bodyPart) : null;
        BodyPartEntity bodyPartEntity = null;

        if (bodyPartEnum != null) {
            bodyPartEntity = bodyPartRepository.findByName(bodyPartEnum).get();
        }

        Page<TrainingProgramEntity> trainingPrograms;

        boolean validTitle = title != null && !title.isEmpty();

        if (!validTitle && bodyPartEnum == null) {
            trainingPrograms = trainingProgramRepository.findAll(paging);
        } else if (validTitle && bodyPartEnum == null) {
            trainingPrograms = trainingProgramRepository.findAllByTitleContaining(title, paging);
        } else if (!validTitle && bodyPartEnum != null) {
            trainingPrograms = trainingProgramRepository.findAllByBodyPartsIn(List.of(bodyPartEntity), paging);
        } else {
            trainingPrograms = trainingProgramRepository
                    .findAllByTitleContainingAndBodyPartsIn(title, List.of(bodyPartEntity), paging);
        }

        return trainingPrograms
                .stream()
                .map(this::toOverview)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public TrainingProgramDetailsViewModel getTrainingProgram(long id) {
        TrainingProgramEntity entity = this.trainingProgramRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Training program with id " + id + " does not exist!"));

        return toDetails(entity);
    }

    @Override
    @Transactional
    public boolean canModify(String email, long trainingId) {
        UserEntity userEntity = this.userRepository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("User with email " + email + " does not exist!"));

        TrainingProgramEntity trainingProgram = this.trainingProgramRepository.findById(trainingId)
                .orElseThrow(() -> new ResourceNotFoundException("Training program with id " + trainingId + " does not exist!"));

        return isAdmin(userEntity) || isOwner(userEntity, trainingProgram);
    }

    @Override
    @Transactional
    public boolean isInUserFavourites(String email, long trainingId) {
        UserEntity userEntity = this.userRepository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("User with email " + email + " does not exist!"));

        TrainingProgramEntity trainingProgram = this.trainingProgramRepository.findById(trainingId)
                .orElseThrow(() -> new ResourceNotFoundException("Training program with id " + trainingId + " does not exist!"));

        return userEntity.getFavouriteTrainingPrograms().contains(trainingProgram);
    }

    @Override
    public void deleteProgram(long id) {
        this.trainingProgramRepository.deleteById(id);
    }

    @Override
    public TrainingProgramServiceModel updateProgram(TrainingProgramServiceModel serviceModel) {
        List<ExerciseEntity> exercises = new ArrayList<>(getExerciseEntities(serviceModel));
        List<BodyPartEntity> bodyParts = new ArrayList<>(getBodyPartEntities(serviceModel));
        UserEntity author = getAuthor(serviceModel);
        GoalEntity goalEntity = getGoalEntity(serviceModel);
        TrainingProgramEntity trainingProgram = buildTrainingProgramEntity(serviceModel, exercises,
                bodyParts, author, goalEntity);

        if (trainingProgram.getId() == 0) {
            trainingProgram.setCreated(LocalDateTime.now());
        }

        this.trainingProgramRepository.save(trainingProgram);
        return serviceModel;
    }

    @Override
    @Transactional
    public void addToFavourites(long id, String userEmail) {
        UserEntity userEntity = this.userRepository.findByEmail(userEmail)
                .orElseThrow(() -> new ResourceNotFoundException("User with email " + userEmail + " does not exist!"));

        TrainingProgramEntity trainingProgram = this.trainingProgramRepository
                .findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Training program with id " + id + " does not exist!"));

        userEntity.addFavouriteTrainingProgram(trainingProgram);
        this.userRepository.save(userEntity);
    }

    @Override
    @Transactional
    public void removeFromFavourites(long id, String userEmail) {
        UserEntity userEntity = this.userRepository.findByEmail(userEmail)
                .orElseThrow(() -> new ResourceNotFoundException("User with email " + userEmail + " does not exist!"));

        TrainingProgramEntity trainingProgram = this.trainingProgramRepository
                .findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Training program with id " + id + " does not exist!"));

        userEntity.getFavouriteTrainingPrograms().remove(trainingProgram);
        this.userRepository.save(userEntity);
    }

    @Override
    public CommentAddServiceModel addComment(CommentAddServiceModel serviceModel, long id) {
        UserEntity author = userRepository.findByEmail(serviceModel.getAuthor())
                .orElseThrow(() -> new ResourceNotFoundException("User with email " + serviceModel.getAuthor() + " does not exist!"));

        TrainingProgramEntity trainingProgram = this.trainingProgramRepository
                .findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Training program with id " + id + " does not exist!"));

        CommentEntity commentEntity = this.modelMapper.map(serviceModel, CommentEntity.class)
                .setAuthor(author)
                .setTrainingProgram(trainingProgram)
                .setApproved(false);

        this.commentRepository.save(commentEntity);
        return serviceModel;
    }

    @Override
    public List<CommentViewModel> getComments(long id) {
        TrainingProgramEntity trainingProgram = this.trainingProgramRepository
                .findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Training program with id " + id + " does not exist!"));

        return this.commentRepository
                .findAllByTrainingProgramAndApproved(trainingProgram, true)
                .stream()
                .map(this::toCommentViewModel)
                .toList();
    }

    @Transactional
    @Override
    public int getLastPageNumber(String title, String bodyPart, Integer pageSize) {

        BodyPartEnum bodyPartEnum = bodyPart != null && !bodyPart.equals("") ? BodyPartEnum.valueOf(bodyPart) : null;
        BodyPartEntity bodyPartEntity = null;

        if (bodyPartEnum != null) {
            bodyPartEntity = bodyPartRepository.findByName(bodyPartEnum).get();
        }

        boolean validTitle = title != null && !title.isEmpty();

        long resultsCount = 0;

        if (!validTitle && bodyPartEnum == null) {
            resultsCount = trainingProgramRepository.count();
        } else if (validTitle && bodyPartEnum == null) {
            resultsCount = trainingProgramRepository.findCountByContainingTitle(title);
        } else if (!validTitle && bodyPartEnum != null) {
            resultsCount = trainingProgramRepository.findAllByBodyPartsIn(List.of(bodyPartEntity)).size();
        } else {
            resultsCount = trainingProgramRepository
                    .findAllByTitleContainingAndBodyPartsIn(title, List.of(bodyPartEntity)).size();
        }

        return (int) Math.ceil(1.0 * resultsCount / pageSize);
    }

    @Transactional
    @Override
    public void removeExerciseFromTraining(long id, String exerciseName) {
        TrainingProgramEntity trainingProgram = trainingProgramRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Training program with id " + id + " does not exist!"));

        trainingProgram.getExercises().removeIf(e -> e.getName().equals(exerciseName));
        trainingProgramRepository.save(trainingProgram);
    }

    private CommentViewModel toCommentViewModel(CommentEntity commentEntity) {
        return new CommentViewModel()
                .setAuthor(commentEntity.getAuthor().getFirstName() + " " + commentEntity.getAuthor().getLastName())
                .setCreated(commentEntity.getCreated().format(DateTimeFormatter.ofPattern("HH:mm dd/MM/yyyy")))
                .setTextContent(commentEntity.getTextContent());
    }

    private TrainingProgramEntity buildTrainingProgramEntity(TrainingProgramServiceModel serviceModel,
                                                             List<ExerciseEntity> exercises,
                                                             List<BodyPartEntity> bodyParts,
                                                             UserEntity author,
                                                             GoalEntity goalEntity) {

        if (bodyParts.isEmpty()) {
            bodyParts = List.of(bodyPartRepository.findByName(BodyPartEnum.OTHER).get());
        }

        return this.trainingProgramRepository
                .findById(serviceModel.getId())
                .orElseGet(TrainingProgramEntity::new)
                .setTitle(serviceModel.getTitle())
                .setDescription(serviceModel.getDescription())
                .setAuthor(author)
                .setGoal(goalEntity)
                .setBodyParts(bodyParts)
                .setExercises(exercises);
    }

    private GoalEntity getGoalEntity(TrainingProgramServiceModel serviceModel) {
        return this.goalRepository.findByName(serviceModel.getGoal())
                .orElseThrow(() -> new ResourceNotFoundException(serviceModel.getGoal() + " is not a valid goal!"));
    }

    private UserEntity getAuthor(TrainingProgramServiceModel serviceModel) {
        return this.userRepository
                .findByEmail(serviceModel.getAuthor())
                .orElseThrow(() -> new ResourceNotFoundException("User with email " + serviceModel.getAuthor() + " does not exist!"));
    }

    private List<BodyPartEntity> getBodyPartEntities(TrainingProgramServiceModel serviceModel) {
        return serviceModel.getBodyParts() != null
                ? serviceModel.getBodyParts()
                .stream()
                .map(bp -> this.bodyPartRepository
                        .findByName(bp)
                        .orElseThrow(() -> new ResourceNotFoundException(bp + " is not a valid body part!")))
                .toList()
                : List.of(this.bodyPartRepository.findByName(BodyPartEnum.OTHER).get());
    }

    private List<ExerciseEntity> getExerciseEntities(TrainingProgramServiceModel serviceModel) {
        return serviceModel.getExercises()
                .stream()
                .map(e -> this.modelMapper.map(e, ExerciseEntity.class))
                .toList();
    }

    private boolean isAdmin(UserEntity userEntity) {
        return userEntity.getRoles()
                .stream()
                .map(RoleEntity::getRole)
                .anyMatch(r -> r.equals(RoleEnum.ADMIN));
    }

    private boolean isOwner(UserEntity userEntity, TrainingProgramEntity trainingProgram) {
        return userEntity.getId() == trainingProgram.getAuthor().getId();
    }

    protected TrainingProgramsOverviewViewModel toOverview(TrainingProgramEntity entity) {
        return new TrainingProgramsOverviewViewModel()
                .setId(entity.getId())
                .setTitle(entity.getTitle())
                .setDescription(entity.getDescription() != null && entity.getDescription().length() > 50
                        ? entity.getDescription().substring(0, 50) + "..."
                        : entity.getDescription())
                .setPictureUrl(entity.getBodyParts().get(0).getPictureUrl())
                .setAuthor(entity.getAuthor().getFirstName() + " " + entity.getAuthor().getLastName())
                .setCreated(entity.getCreated().format(DateTimeFormatter.ofPattern("HH:mm dd/MM/yyyy")));
    }

    private TrainingProgramDetailsViewModel toDetails(TrainingProgramEntity entity) {
        List<ExerciseOverviewViewModel> exercises = entity.getExercises()
                .stream()
                .map(e -> this.modelMapper.map(e, ExerciseOverviewViewModel.class))
                .toList();

        List<BodyPartEnum> bodyParts = entity.getBodyParts()
                .stream()
                .map(BodyPartEntity::getName)
                .toList();

        return new TrainingProgramDetailsViewModel()
                .setId(entity.getId())
                .setTitle(entity.getTitle())
                .setDescription(entity.getDescription())
                .setGoal(entity.getGoal().getName())
                .setBodyParts(bodyParts)
                .setPictureUrl(entity.getBodyParts().get(0).getPictureUrl())
                .setAuthor(entity.getAuthor().getFirstName() + " " + entity.getAuthor().getLastName())
                .setCreated(entity.getCreated().format(DateTimeFormatter.ofPattern("HH:mm dd/MM/yyyy")))
                .setExercises(exercises);
    }
}
