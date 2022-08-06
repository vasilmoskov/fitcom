package bg.softuni.fitcom.services;

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
import bg.softuni.fitcom.services.impl.DietServiceImpl;
import bg.softuni.fitcom.services.impl.TrainingProgramServiceImpl;
import bg.softuni.fitcom.services.impl.UserServiceImpl;
import bg.softuni.fitcom.utils.TestServiceUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class UserServiceTest {


    @Mock
    private UserRepository userRepository;
    @Mock
    private RoleRepository roleRepository;
    @Mock
    private TrainingProgramRepository trainingProgramRepository;
    @Mock
    private DietRepository dietRepository;

    @Mock
    private TrainingProgramServiceImpl trainingProgramService;
    @Mock
    private DietServiceImpl dietService;
    @Mock
    private FitComUserService userDetailsService;

    @Mock
    private ModelMapper modelMapper;
    @Mock
    private PasswordEncoder passwordEncoder;

    private TestServiceUtils testServiceUtils;

    private UserService serviceToTest;

    @BeforeEach
    void setup() {
        testServiceUtils = new TestServiceUtils();

        serviceToTest = new UserServiceImpl(userRepository, trainingProgramRepository, dietRepository,
                roleRepository, modelMapper, passwordEncoder, trainingProgramService, dietService, userDetailsService);
    }

    @Test
    void testGetFirstName() {
        UserEntity user = testServiceUtils.createUser();

        when(userRepository.findByEmail("georgi@abv.bg")).thenReturn(Optional.of(user));

        String firstName = serviceToTest.getFirstName("georgi@abv.bg");

        assertEquals("Georgi", firstName);
    }

    @Test
    void testGetUser() {
        UserEntity user = testServiceUtils.createUser();

        when(userRepository.findByEmail("georgi@abv.bg")).thenReturn(Optional.of(user));

        UserEntity result = serviceToTest.getUser("georgi@abv.bg");

        assertEquals(user, result);
    }

    @Test
    void testRegister() {
        UserRegisterServiceModel registerServiceModel = testServiceUtils.createRegisterServiceModel();
        UserEntity userEntity = new UserEntity();

        when(roleRepository.findByRole(RoleEnum.TRAINER))
                .thenReturn(Optional.ofNullable(new RoleEntity().setRole(RoleEnum.TRAINER)));
        when(roleRepository.findByRole(RoleEnum.USER))
                .thenReturn(Optional.ofNullable(new RoleEntity().setRole(RoleEnum.USER)));
        when(modelMapper.map(registerServiceModel, UserEntity.class)).thenReturn(userEntity);

        serviceToTest.register(registerServiceModel);

        verify(userRepository, times(1)).save(userEntity);
    }

    @Test
    void testGetProfile() {
        UserEntity user = testServiceUtils.createUser();
        ProfileViewModel profileViewModel = testServiceUtils.createProfileViewModel();

        when(userRepository.findByEmail("georgi@abv.bg")).thenReturn(Optional.of(user));
        when(modelMapper.map(user, ProfileViewModel.class)).thenReturn(profileViewModel);

        ProfileViewModel result = serviceToTest.getProfile("georgi@abv.bg");

        assertEquals(profileViewModel, result);
    }

    @Test
    void testGetFavouritePrograms() {
        UserEntity user = testServiceUtils.createUser();

        when(userRepository.findByEmail("georgi@abv.bg")).thenReturn(Optional.of(user));

        List<TrainingProgramsOverviewViewModel> favouriteTrainingPrograms = serviceToTest.getFavouriteTrainingPrograms("georgi@abv.bg");

        assertTrue(favouriteTrainingPrograms.isEmpty());
    }

    @Test
    void testEditProfile() {
        UserEntity user = testServiceUtils.createUser();
        ProfileEditServiceModel profileEditServiceModel = testServiceUtils.createProfileEditServiceModel();

        when(userRepository.findByEmail("georgi@abv.bg")).thenReturn(Optional.of(user));

        serviceToTest.editProfile(profileEditServiceModel);

        verify(userRepository, times(1)).save(user);
        verify(userDetailsService, times(1)).loadUserByUsername("georgi@abv.bg");
    }

    @Test
    void testGetPostedTrainingPrograms() {

        when(trainingProgramRepository.findAllByAuthorEmail("georgi@abv.bg")).thenReturn(new ArrayList<>());

        List<TrainingProgramsOverviewViewModel> postedTrainingPrograms = serviceToTest
                .getPostedTrainingPrograms("georgi@abv.bg");

        assertTrue(postedTrainingPrograms.isEmpty());
    }

    @Test
    void testGetFavouriteDiets() {
        UserEntity user = testServiceUtils.createUser();

        when(userRepository.findByEmail("georgi@abv.bg")).thenReturn(Optional.of(user));

        List<DietOverviewViewModel> favouriteDiets = serviceToTest.getFavouriteDiets("georgi@abv.bg");

        assertTrue(favouriteDiets.isEmpty());
    }

    @Test
    void testGetPostedDiets() {
        when(dietRepository.findAllByAuthorEmail("georgi@abv.bg")).thenReturn(new ArrayList<>());

        List<DietOverviewViewModel> postedDiets = serviceToTest.getPostedDiets("georgi@abv.bg");

        assertTrue(postedDiets.isEmpty());
    }

    @Test
    void testIsAdminReturnsFalse() {
        UserEntity user = testServiceUtils.createUser();

        when(userRepository.findByEmail("georgi@abv.bg")).thenReturn(Optional.of(user));

        boolean isAdmin = serviceToTest.isAdmin("georgi@abv.bg");

        assertFalse(isAdmin);
    }

    @Test
    void testIsAdminReturnsTrueForAdmin() {
        UserEntity user = testServiceUtils.createAdmin();

        when(userRepository.findByEmail("admin@abv.bg")).thenReturn(Optional.of(user));

        boolean isAdmin = serviceToTest.isAdmin("admin@abv.bg");

        assertTrue(isAdmin);
    }

    @Test
    void testHasAppliedReturnsFalseWhenUserHasNotAppliedForRole() {
        UserEntity user = testServiceUtils.createUser();

        when(userRepository.findByEmail("georgi@abv.bg")).thenReturn(Optional.of(user));
        when(roleRepository.findByRole(RoleEnum.NUTRITIONIST))
                .thenReturn(Optional.ofNullable(new RoleEntity().setRole(RoleEnum.NUTRITIONIST)));

        boolean hasApplied = serviceToTest.hasApplied("georgi@abv.bg", RoleEnum.NUTRITIONIST);

        assertFalse(hasApplied);
    }

    @Test
    void testHasAppliedReturnsTrueWhenUserHasAppliedForRole() {
        RoleEntity nutritionistRole = new RoleEntity().setRole(RoleEnum.NUTRITIONIST);
        UserEntity user = testServiceUtils.createUser()
                .setPendingRoles(List.of(nutritionistRole));

        when(userRepository.findByEmail("georgi@abv.bg")).thenReturn(Optional.of(user));
        when(roleRepository.findByRole(RoleEnum.NUTRITIONIST))
                .thenReturn(Optional.of(nutritionistRole));

        boolean hasApplied = serviceToTest.hasApplied("georgi@abv.bg", RoleEnum.NUTRITIONIST);

        assertTrue(hasApplied);
    }

    @Test
    void testApplyForNutritionist() {
        UserEntity user = testServiceUtils.createUser();

        when(userRepository.findByEmail("georgi@abv.bg")).thenReturn(Optional.of(user));
        when(roleRepository.findByRole(RoleEnum.NUTRITIONIST))
                .thenReturn(Optional.ofNullable(new RoleEntity().setRole(RoleEnum.NUTRITIONIST)));

        serviceToTest.applyForNutritionist("georgi@abv.bg");

        verify(userRepository, times(1)).save(user);
    }

    @Test
    void testApplyForNutritionistReturnsWhenUserHasApplied() {
        RoleEntity nutritionistRole = new RoleEntity().setRole(RoleEnum.NUTRITIONIST);
        UserEntity user = testServiceUtils.createUser()
                .setPendingRoles(List.of(nutritionistRole));

        when(userRepository.findByEmail("georgi@abv.bg")).thenReturn(Optional.of(user));
        when(roleRepository.findByRole(RoleEnum.NUTRITIONIST))
                .thenReturn(Optional.of(nutritionistRole));

        serviceToTest.applyForNutritionist("georgi@abv.bg");

        verify(userRepository, times(0)).save(user);
    }

    @Test
    void testApplyForTrainer() {
        UserEntity user = testServiceUtils.createAnotherUser();

        when(userRepository.findByEmail("pesho@abv.bg")).thenReturn(Optional.of(user));
        when(roleRepository.findByRole(RoleEnum.TRAINER))
                .thenReturn(Optional.ofNullable(new RoleEntity().setRole(RoleEnum.TRAINER)));

        serviceToTest.applyForTrainer("pesho@abv.bg");

        verify(userRepository, times(1)).save(user);
    }

    @Test
    void testApplyForTrainerReturnsWhenUserHasApplied() {
        RoleEntity trainerRole = new RoleEntity().setRole(RoleEnum.TRAINER);
        UserEntity user = testServiceUtils.createAnotherUser()
                .setPendingRoles(List.of(trainerRole));

        when(userRepository.findByEmail("pesho@abv.bg")).thenReturn(Optional.of(user));
        when(roleRepository.findByRole(RoleEnum.TRAINER))
                .thenReturn(Optional.of(trainerRole));

        serviceToTest.applyForTrainer("pesho@abv.bg");

        verify(userRepository, times(0)).save(user);
    }

    @Test
    void testGetApplications() {
        RoleEntity trainerRole = new RoleEntity().setRole(RoleEnum.TRAINER);
        UserEntity user = testServiceUtils.createAnotherUser()
                .setPendingRoles(List.of(trainerRole));

        when(userRepository.findAllByPendingRolesNotEmpty()).thenReturn(List.of(user));

        List<ApplicationsViewModel> applications = serviceToTest.getApplications();

        assertEquals(1, applications.size());
    }

    @Test
    void testApproveApplication() {
        RoleEntity trainerRole = new RoleEntity().setRole(RoleEnum.TRAINER);
        List<RoleEntity> pendingRoles = new ArrayList<>();
        pendingRoles.add(trainerRole);

        UserEntity user = testServiceUtils.createAnotherUser().setPendingRoles(pendingRoles);

        when(userRepository.findById(2L)).thenReturn(Optional.of(user));

        serviceToTest.approveApplication(2);

        verify(userRepository, times(1)).save(user);
    }

    @Test
    void testDeleteApplication() {
        RoleEntity trainerRole = new RoleEntity().setRole(RoleEnum.TRAINER);
        List<RoleEntity> pendingRoles = new ArrayList<>();
        pendingRoles.add(trainerRole);

        UserEntity user = testServiceUtils.createAnotherUser().setPendingRoles(pendingRoles);

        when(userRepository.findById(2L)).thenReturn(Optional.of(user));

        serviceToTest.deleteApplication(2);

        verify(userRepository, times(1)).save(user);
    }
}
