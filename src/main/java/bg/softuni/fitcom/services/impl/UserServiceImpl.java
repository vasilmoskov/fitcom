package bg.softuni.fitcom.services.impl;

import bg.softuni.fitcom.exceptions.ResourceNotFoundException;
import bg.softuni.fitcom.models.entities.RoleEntity;
import bg.softuni.fitcom.models.entities.UserEntity;
import bg.softuni.fitcom.models.enums.RoleEnum;
import bg.softuni.fitcom.models.service.ProfileEditServiceModel;
import bg.softuni.fitcom.models.service.UserRegisterServiceModel;
import bg.softuni.fitcom.models.user.FitComUserService;
import bg.softuni.fitcom.models.view.ApplicationsViewModel;
import bg.softuni.fitcom.models.view.DietOverviewViewModel;
import bg.softuni.fitcom.models.view.ProfileViewModel;
import bg.softuni.fitcom.models.view.TrainingProgramsOverviewViewModel;
import bg.softuni.fitcom.repositories.DietRepository;
import bg.softuni.fitcom.repositories.RoleRepository;
import bg.softuni.fitcom.repositories.TrainingProgramRepository;
import bg.softuni.fitcom.repositories.UserRepository;
import bg.softuni.fitcom.services.UserService;
import org.modelmapper.ModelMapper;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final TrainingProgramRepository trainingProgramRepository;
    private final DietRepository dietRepository;

    private final TrainingProgramServiceImpl trainingProgramService;
    private final DietServiceImpl dietService;
    private final FitComUserService userDetailsService;

    private final ModelMapper modelMapper;
    private final PasswordEncoder passwordEncoder;

    public UserServiceImpl(UserRepository userRepository, TrainingProgramRepository trainingProgramRepository, DietRepository dietRepository, RoleRepository roleRepository, ModelMapper modelMapper, PasswordEncoder passwordEncoder, TrainingProgramServiceImpl trainingProgramService, DietServiceImpl dietService, FitComUserService userDetailsService) {
        this.userRepository = userRepository;
        this.trainingProgramRepository = trainingProgramRepository;
        this.dietRepository = dietRepository;
        this.roleRepository = roleRepository;
        this.modelMapper = modelMapper;
        this.passwordEncoder = passwordEncoder;
        this.trainingProgramService = trainingProgramService;
        this.dietService = dietService;
        this.userDetailsService = userDetailsService;
    }

    @Override
    public String getFirstName(String email) {
        UserEntity userEntity = this.userRepository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("User with email " + email + " does not exist!"));

        String firstName = userEntity.getFirstName();

        return firstName != null && !"".equals(firstName)
                ? firstName
                : "Anonymous";
    }

    @Override
    public UserEntity getUser(String email) {
        return this.userRepository.findByEmail(email).orElse(null);
    }

    @Override
    public UserRegisterServiceModel register(UserRegisterServiceModel serviceModel) {
        List<RoleEntity> roleEntities = serviceModel.
                getRoles()
                .stream()
                .map(r -> this.roleRepository.findByRole(r)
                        .orElseThrow(() -> new ResourceNotFoundException(r + " is not a valid role!")))
                .collect(Collectors.toList());

        roleEntities.add(this.roleRepository.findByRole(RoleEnum.USER).get());

        UserEntity userEntity = this.modelMapper.map(serviceModel, UserEntity.class)
                .setPassword(passwordEncoder.encode(serviceModel.getPassword()))
                .setRoles(roleEntities);

        this.userRepository.save(userEntity);
        return serviceModel;
    }

    @Override
    @Transactional
    public ProfileViewModel getProfile(String email) {
        UserEntity userEntity = this.userRepository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("User with email " + email + " does not exist!"));

        List<RoleEnum> roles = userEntity
                .getRoles()
                .stream()
                .map(RoleEntity::getRole)
                .toList();

        return this.modelMapper.map(userEntity, ProfileViewModel.class)
                .setRoles(roles);
    }

    @Override
    @Transactional
    public List<TrainingProgramsOverviewViewModel> getFavouriteTrainingPrograms(String email) {
        UserEntity userEntity = this.userRepository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("User with email " + email + " does not exist!"));

        return userEntity.getFavouriteTrainingPrograms()
                .stream()
                .map(this.trainingProgramService::toOverview)
                .toList();
    }

    @Override
    public ProfileEditServiceModel editProfile(ProfileEditServiceModel serviceModel) {
        UserEntity userEntity = this.userRepository.findByEmail(serviceModel.getEmail())
                .orElseThrow(() -> new ResourceNotFoundException("User with email " + serviceModel.getEmail() + " does not exist!"));

        userEntity.setFirstName(serviceModel.getFirstName())
                .setLastName(serviceModel.getLastName())
                .setAge(serviceModel.getAge());

        if (serviceModel.getPicturePublicId() != null && serviceModel.getPictureUrl() != null) {
            userEntity.setPictureUrl(serviceModel.getPictureUrl())
                    .setPicturePublicId(serviceModel.getPicturePublicId());
        }

        this.userRepository.save(userEntity);
        this.userDetailsService.loadUserByUsername(serviceModel.getEmail());
        return serviceModel;
    }

    @Transactional
    @Override
    public List<TrainingProgramsOverviewViewModel> getPostedTrainingPrograms(String email) {
        return this.trainingProgramRepository
                .findAllByAuthorEmail(email)
                .stream()
                .map(this.trainingProgramService::toOverview)
                .toList();
    }

    @Override
    @Transactional
    public List<DietOverviewViewModel> getFavouriteDiets(String email) {
        UserEntity userEntity = this.userRepository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("User with email " + email + " does not exist!"));

        return userEntity.getFavouriteDiets()
                .stream()
                .map(this.dietService::toOverview)
                .toList();
    }

    @Override
    public List<DietOverviewViewModel> getPostedDiets(String email) {
        return this.dietRepository
                .findAllByAuthorEmail(email)
                .stream()
                .map(this.dietService::toOverview)
                .toList();
    }

    @Override
    @Transactional
    public boolean isAdmin(String email) {
        return this.userRepository
                .findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("User with email " + email + " does not exist!"))
                .getRoles()
                .stream()
                .map(RoleEntity::getRole)
                .anyMatch(r -> r.equals(RoleEnum.ADMIN));
    }

    @Override
    @Transactional
    public boolean hasApplied(String email, RoleEnum role) {
        UserEntity userEntity = this.userRepository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("User with email " + email + " does not exist!"));

        RoleEntity roleEntity = this.roleRepository.findByRole(role)
                .orElseThrow(() -> new ResourceNotFoundException(role + " is not a valid role!"));

        return userEntity.getPendingRoles().contains(roleEntity) || userEntity.getRoles().contains(roleEntity);
    }

    @Override
    @Transactional
    public void applyForNutritionist(String name) {
        if (hasApplied(name, RoleEnum.NUTRITIONIST)) {
            return;
        }

        UserEntity userEntity = this.userRepository.findByEmail(name)
                .orElseThrow(() -> new ResourceNotFoundException("User with email " + name + " does not exist!"));
        RoleEntity nutritionistRole = this.roleRepository.findByRole(RoleEnum.NUTRITIONIST).get();
        userEntity.addPendingRole(nutritionistRole);
        this.userRepository.save(userEntity);
    }

    @Override
    @Transactional
    public void applyForTrainer(String name) {
        if (hasApplied(name, RoleEnum.TRAINER)) {
            return;
        }

        UserEntity userEntity = this.userRepository.findByEmail(name)
                .orElseThrow(() -> new ResourceNotFoundException("User with email " + name + " does not exist!"));

        RoleEntity trainerRole = this.roleRepository.findByRole(RoleEnum.TRAINER).get();

        userEntity.addPendingRole(trainerRole);

        this.userRepository.save(userEntity);
    }

    @Override
    @Transactional
    public List<ApplicationsViewModel> getApplications() {
        return this.userRepository.findAllByPendingRolesNotEmpty()
                .stream()
                .map(this::toApplication)
                .toList();
    }

    @Override
    @Transactional
    public void approveApplication(long userId) {
        UserEntity userEntity = this.userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User with id " + userId + " does not exist!"));

        userEntity.getPendingRoles()
                .forEach(userEntity::addRole);

        userEntity.setPendingRoles(new ArrayList<>());
        this.userRepository.save(userEntity);
    }

    @Override
    public void deleteApplication(long userId) {
        UserEntity userEntity = this.userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User with id " + userId + " does not exist!"));

        userEntity.setPendingRoles(new ArrayList<>());
        this.userRepository.save(userEntity);
    }

    private ApplicationsViewModel toApplication(UserEntity user) {
        List<RoleEnum> roles = user.getPendingRoles()
                .stream()
                .map(RoleEntity::getRole)
                .collect(Collectors.toList());

        return new ApplicationsViewModel()
                .setUserId(user.getId())
                .setUser(user.getFirstName() + " " + user.getLastName())
                .setRoles(roles);
    }
}
