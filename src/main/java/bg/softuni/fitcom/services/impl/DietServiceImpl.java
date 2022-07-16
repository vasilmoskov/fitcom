package bg.softuni.fitcom.services.impl;

import bg.softuni.fitcom.exceptions.ResourceNotFoundException;
import bg.softuni.fitcom.models.entities.CommentEntity;
import bg.softuni.fitcom.models.entities.DietEntity;
import bg.softuni.fitcom.models.entities.PurposeEntity;
import bg.softuni.fitcom.models.entities.RoleEntity;
import bg.softuni.fitcom.models.entities.UserEntity;
import bg.softuni.fitcom.models.enums.RoleEnum;
import bg.softuni.fitcom.models.service.CommentAddServiceModel;
import bg.softuni.fitcom.models.service.DietServiceModel;
import bg.softuni.fitcom.models.view.CommentViewModel;
import bg.softuni.fitcom.models.view.DietDetailsViewModel;
import bg.softuni.fitcom.models.view.DietOverviewViewModel;
import bg.softuni.fitcom.repositories.CommentRepository;
import bg.softuni.fitcom.repositories.DietRepository;
import bg.softuni.fitcom.repositories.PurposeRepository;
import bg.softuni.fitcom.repositories.UserRepository;
import bg.softuni.fitcom.services.DietService;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Service
public class DietServiceImpl implements DietService {
    private final DietRepository dietRepository;
    private final UserRepository userRepository;
    private final PurposeRepository purposeRepository;
    private final CommentRepository commentRepository;
    private final ModelMapper modelMapper;

    public DietServiceImpl(DietRepository dietRepository, UserRepository userRepository,
                           PurposeRepository purposeRepository, CommentRepository commentRepository,
                           ModelMapper modelMapper) {
        this.dietRepository = dietRepository;
        this.userRepository = userRepository;
        this.purposeRepository = purposeRepository;
        this.commentRepository = commentRepository;
        this.modelMapper = modelMapper;
    }

    @Override
    public List<DietOverviewViewModel> getDiets() {
        return this.dietRepository.findAll()
                .stream()
                .map(this::toOverview)
                .toList();
    }

    @Override
    public DietServiceModel updateDiet(DietServiceModel serviceModel) {
        UserEntity userEntity = this.userRepository.findByEmail(serviceModel.getAuthor())
                .orElseThrow(() -> new ResourceNotFoundException("No such user!"));

        PurposeEntity purposeEntity = this.purposeRepository.findByName(serviceModel.getPurpose())
                .orElseThrow(() -> new ResourceNotFoundException("No such purpose!"));

        DietEntity dietEntity = this.dietRepository
                .findById(serviceModel.getId())
                .orElseGet(DietEntity::new)
                .setTitle(serviceModel.getTitle())
                .setDescription(serviceModel.getDescription())
                .setAuthor(userEntity)
                .setPurpose(purposeEntity);

        if (dietEntity.getId() == 0) {
            dietEntity.setCreated(LocalDateTime.now());
        }

        this.dietRepository.save(dietEntity);
        return serviceModel;
    }

    @Override
    public DietDetailsViewModel getDiet(long id) {
        DietEntity dietEntity = this.dietRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("No such diet"));

        return toDetails(dietEntity);
    }

    @Override
    @Transactional
    public boolean canModify(String email, long dietId) {
        UserEntity userEntity = this.userRepository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("No such user."));

        DietEntity diet = this.dietRepository.findById(dietId)
                .orElseThrow(() -> new ResourceNotFoundException("No such diet."));

        return isAdmin(userEntity) || isOwner(userEntity, diet);
    }

    @Override
    @Transactional
    public boolean isInUserFavourites(String email, long dietId) {
        UserEntity userEntity = this.userRepository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("No such user."));

        DietEntity diet = this.dietRepository.findById(dietId)
                .orElseThrow(() -> new ResourceNotFoundException("No such diet."));

        return userEntity.getFavouriteDiets().contains(diet);
    }

    @Override
    public void deleteDiet(long id) {
        this.dietRepository.deleteById(id);
    }

    @Override
    @Transactional
    public void addToFavourites(long id, String userEmail) {
        UserEntity userEntity = this.userRepository.findByEmail(userEmail)
                .orElseThrow(() -> new ResourceNotFoundException("No such user"));

        DietEntity diet = this.dietRepository
                .findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("No such diet."));

        userEntity.addFavouriteDiet(diet);
        this.userRepository.save(userEntity);
    }

    @Override
    @Transactional
    public void removeFromFavourites(long id, String userEmail) {
        UserEntity userEntity = this.userRepository.findByEmail(userEmail)
                .orElseThrow(() -> new ResourceNotFoundException("No such user"));

        DietEntity diet = this.dietRepository
                .findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("No such diet."));

        userEntity.getFavouriteDiets().remove(diet);
        this.userRepository.save(userEntity);
    }

    @Override
    public CommentAddServiceModel addComment(CommentAddServiceModel serviceModel, long id) {
        UserEntity author = userRepository.findByEmail(serviceModel.getAuthor())
                .orElseThrow(() -> new ResourceNotFoundException("No such user"));

        DietEntity diet = this.dietRepository
                .findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("No such diet."));

        CommentEntity commentEntity = this.modelMapper.map(serviceModel, CommentEntity.class)
                .setAuthor(author)
                .setDiet(diet)
                .setApproved(false);

        this.commentRepository.save(commentEntity);
        return serviceModel;
    }

    @Override
    public List<CommentViewModel> getComments(long id) {
        DietEntity diet = this.dietRepository
                .findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("No such diet."));

        return this.commentRepository
                .findAllByDietAndApproved(diet, true)
                .stream()
                .map(this::toCommentViewModel)
                .toList();
    }

    protected DietOverviewViewModel toOverview(DietEntity dietEntity) {
        return new DietOverviewViewModel()
                .setId(dietEntity.getId())
                .setTitle(dietEntity.getTitle())
                .setAuthor(String.format("%s %s",
                        dietEntity.getAuthor().getFirstName(),
                        dietEntity.getAuthor().getLastName()))
                .setDescription(dietEntity.getDescription().length() > 50
                        ? dietEntity.getDescription().substring(0, 50) + "..."
                        : dietEntity.getDescription())
                .setCreated(dietEntity.getCreated().format(DateTimeFormatter.ofPattern("HH:mm dd/MM/yyyy")));
    }

    private CommentViewModel toCommentViewModel(CommentEntity commentEntity) {
        return new CommentViewModel()
                .setAuthor(commentEntity.getAuthor().getFirstName() + " " + commentEntity.getAuthor().getLastName())
                .setCreated(commentEntity.getCreated().format(DateTimeFormatter.ofPattern("HH:mm dd/MM/yyyy")))
                .setTextContent(commentEntity.getTextContent());
    }

    private DietDetailsViewModel toDetails(DietEntity dietEntity) {
        return new DietDetailsViewModel()
                .setId(dietEntity.getId())
                .setTitle(dietEntity.getTitle())
                .setDescription(dietEntity.getDescription())
                .setPurpose(dietEntity.getPurpose().getName())
                .setAuthor(dietEntity.getAuthor().getFirstName() + " " + dietEntity.getAuthor().getLastName())
                .setCreated(dietEntity.getCreated().format(DateTimeFormatter.ofPattern("HH:mm dd/MM/yyyy")));
    }

    private boolean isAdmin(UserEntity userEntity) {
        return userEntity.getRoles()
                .stream()
                .map(RoleEntity::getRole)
                .anyMatch(r -> r.equals(RoleEnum.ADMIN));
    }

    private boolean isOwner(UserEntity userEntity, DietEntity diet) {
        return userEntity.getId() == diet.getAuthor().getId();
    }
}