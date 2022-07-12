package bg.softuni.fitcom.services.impl;

import bg.softuni.fitcom.exceptions.ResourceNotFoundException;
import bg.softuni.fitcom.models.entities.BodyPartEntity;
import bg.softuni.fitcom.models.entities.CommentEntity;
import bg.softuni.fitcom.models.entities.ExerciseEntity;
import bg.softuni.fitcom.models.entities.PurposeEntity;
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
import bg.softuni.fitcom.repositories.ExerciseRepository;
import bg.softuni.fitcom.repositories.PurposeRepository;
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
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class TrainingProgramServiceImpl implements TrainingProgramService {
    private final TrainingProgramRepository trainingProgramRepository;
    private final ExerciseRepository exerciseRepository;
    private final CommentRepository commentRepository;
    private final ModelMapper modelMapper;
    private final UserRepository userRepository;
    private final PurposeRepository purposeRepository;
    private final BodyPartRepository bodyPartRepository;

    public TrainingProgramServiceImpl(TrainingProgramRepository trainingProgramRepository, ExerciseRepository exerciseRepository, CommentRepository commentRepository, ModelMapper modelMapper, UserRepository userRepository, PurposeRepository purposeRepository, BodyPartRepository bodyPartRepository) {
        this.trainingProgramRepository = trainingProgramRepository;
        this.exerciseRepository = exerciseRepository;
        this.commentRepository = commentRepository;
        this.modelMapper = modelMapper;
        this.userRepository = userRepository;
        this.purposeRepository = purposeRepository;
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
                .orElseThrow(() -> new ResourceNotFoundException("Training program does not exist!"));

        return toDetails(entity);
    }

    @Override
    @Transactional
    public boolean canModify(String email, long trainingId) {
        UserEntity userEntity = this.userRepository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("No such user."));

        TrainingProgramEntity trainingProgram = this.trainingProgramRepository.findById(trainingId)
                .orElseThrow(() -> new ResourceNotFoundException("No such training."));

        return isAdmin(userEntity) || isOwner(userEntity, trainingProgram);
    }

    @Override
    @Transactional
    public boolean isInUserFavourites(String email, long trainingId) {
        UserEntity userEntity = this.userRepository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("No such user."));

        TrainingProgramEntity trainingProgram = this.trainingProgramRepository.findById(trainingId)
                .orElseThrow(() -> new ResourceNotFoundException("No such training."));

        return userEntity.getFavouriteTrainingPrograms().contains(trainingProgram);
    }

    @Override
    public void deleteProgram(long id) {
        this.trainingProgramRepository.deleteById(id);
    }

    @Override
//    @Transactional
    public TrainingProgramServiceModel updateProgram(TrainingProgramServiceModel serviceModel) {
        List<ExerciseEntity> exercises = getExerciseEntities(serviceModel);
        List<BodyPartEntity> bodyParts = getBodyPartEntities(serviceModel);
        UserEntity author = getAuthor(serviceModel);
        PurposeEntity purposeEntity = getPurposeEntity(serviceModel);
        TrainingProgramEntity trainingProgram = buildTrainingProgramEntity(serviceModel, exercises,
                bodyParts, author, purposeEntity);

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
                .orElseThrow(() -> new ResourceNotFoundException("No such user"));

        TrainingProgramEntity trainingProgram = this.trainingProgramRepository
                .findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("No such training."));

        userEntity.addFavouriteTrainingProgram(trainingProgram);
        this.userRepository.save(userEntity);
    }

    @Override
    @Transactional
    public void removeFromFavourites(long id, String userEmail) {
        UserEntity userEntity = this.userRepository.findByEmail(userEmail)
                .orElseThrow(() -> new ResourceNotFoundException("No such user"));

        TrainingProgramEntity trainingProgram = this.trainingProgramRepository
                .findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("No such training."));

        userEntity.getFavouriteTrainingPrograms().remove(trainingProgram);
        this.userRepository.save(userEntity);
    }

    @Override
    public CommentAddServiceModel addComment(CommentAddServiceModel serviceModel, long id) {
        UserEntity author = userRepository.findByEmail(serviceModel.getAuthor())
                .orElseThrow(() -> new ResourceNotFoundException("No such user"));

        TrainingProgramEntity trainingProgram = this.trainingProgramRepository
                .findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("No such training."));

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
                .orElseThrow(() -> new ResourceNotFoundException("No such training."));

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
                                                             PurposeEntity purposeEntity) {
        return this.trainingProgramRepository
                .findById(serviceModel.getId())
                .orElseGet(TrainingProgramEntity::new)
                .setTitle(serviceModel.getTitle())
                .setDescription(serviceModel.getDescription())
                .setAuthor(author)
                .setPurpose(purposeEntity)
                .setBodyParts(bodyParts)
                .setExercises(exercises);
    }

    private PurposeEntity getPurposeEntity(TrainingProgramServiceModel serviceModel) {
        return this.purposeRepository.findByName(serviceModel.getPurpose())
                .orElseThrow(() -> new ResourceNotFoundException("No such purpose."));
    }

    private UserEntity getAuthor(TrainingProgramServiceModel serviceModel) {
        return this.userRepository
                .findByEmail(serviceModel.getAuthor())
                .orElseThrow(() -> new ResourceNotFoundException("No such user."));
    }

    private List<BodyPartEntity> getBodyPartEntities(TrainingProgramServiceModel serviceModel) {
        return serviceModel.getBodyParts() != null
                ? serviceModel.getBodyParts()
                .stream()
                .map(bp -> this.bodyPartRepository
                        .findByName(bp)
                        .orElseThrow(() -> new ResourceNotFoundException("No such bodypart.")))
                .toList()
                : List.of(this.bodyPartRepository.findByName(BodyPartEnum.OTHER).get());
    }

    private List<ExerciseEntity> getExerciseEntities(TrainingProgramServiceModel serviceModel) {
        return serviceModel.getExercises()
                .stream()
                .map(e -> {
                    Optional<ExerciseEntity> exerciseOpt = this.exerciseRepository.findByName(e.getName());
                    ExerciseEntity exerciseEntity;

                    if (exerciseOpt.isPresent()) {
                        exerciseEntity = exerciseOpt.get()
                                .setDescription(e.getDescription())
                                .setVideoUrl(e.getVideoUrl());
                    } else {
                        exerciseEntity = this.modelMapper.map(e, ExerciseEntity.class);
                    }

                    return exerciseEntity;
                })
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
                .setDescription(entity.getDescription().length() > 50
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
                .setPurpose(entity.getPurpose().getName())
                .setBodyParts(bodyParts)
                .setPictureUrl(entity.getBodyParts().get(0).getPictureUrl())
                .setAuthor(entity.getAuthor().getFirstName() + " " + entity.getAuthor().getLastName())
                .setCreated(entity.getCreated().format(DateTimeFormatter.ofPattern("HH:mm dd/MM/yyyy")))
                .setExercises(exercises);
    }
}
