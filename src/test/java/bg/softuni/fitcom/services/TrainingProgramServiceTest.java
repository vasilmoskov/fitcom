package bg.softuni.fitcom.services;

import bg.softuni.fitcom.models.entities.BodyPartEntity;
import bg.softuni.fitcom.models.entities.CommentEntity;
import bg.softuni.fitcom.models.entities.TrainingProgramEntity;
import bg.softuni.fitcom.models.entities.UserEntity;
import bg.softuni.fitcom.models.enums.BodyPartEnum;
import bg.softuni.fitcom.models.enums.GoalEnum;
import bg.softuni.fitcom.models.service.CommentAddServiceModel;
import bg.softuni.fitcom.models.service.TrainingProgramServiceModel;
import bg.softuni.fitcom.models.view.CommentViewModel;
import bg.softuni.fitcom.models.view.TrainingProgramDetailsViewModel;
import bg.softuni.fitcom.models.view.TrainingProgramsOverviewViewModel;
import bg.softuni.fitcom.repositories.BodyPartRepository;
import bg.softuni.fitcom.repositories.CommentRepository;
import bg.softuni.fitcom.repositories.GoalRepository;
import bg.softuni.fitcom.repositories.TrainingProgramRepository;
import bg.softuni.fitcom.repositories.UserRepository;
import bg.softuni.fitcom.services.impl.TrainingProgramServiceImpl;
import bg.softuni.fitcom.utils.TestServiceUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class TrainingProgramServiceTest {

    @Mock
    private TrainingProgramRepository trainingProgramRepository;

    @Mock
    private CommentRepository commentRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private GoalRepository goalRepository;

    @Mock
    private BodyPartRepository bodyPartRepository;

    @Mock
    private ModelMapper modelMapper;

    private TestServiceUtils testServiceUtils;

    private TrainingProgramService serviceToTest;

    @BeforeEach
    void setup() {
        testServiceUtils = new TestServiceUtils();

        serviceToTest = new TrainingProgramServiceImpl(trainingProgramRepository, commentRepository,
                userRepository, goalRepository, bodyPartRepository, modelMapper);
    }

    @Test
    void testGetTrainingPrograms() {
        int pageNo = 1;
        int pageSize = 3;
        String sortBy = "id";

        Pageable paging = PageRequest.of(pageNo, pageSize, Sort.by(sortBy));

        List<TrainingProgramEntity> trainingPrograms = testServiceUtils.createTrainingPrograms();
        Page<TrainingProgramEntity> trainingProgramsPage = new PageImpl<>(trainingPrograms);

        when(trainingProgramRepository.findAll(paging)).thenReturn(trainingProgramsPage);

        List<TrainingProgramsOverviewViewModel> viewModel = serviceToTest.getTrainingPrograms(
                null, null, pageNo, pageSize, sortBy);

        assertEquals(2, viewModel.size());
        assertEquals(trainingPrograms.get(0).getTitle(), viewModel.get(0).getTitle());
        assertEquals(trainingPrograms.get(1).getTitle(), viewModel.get(1).getTitle());
    }

    @Test
    void testGetTrainingProgram() {
        TrainingProgramEntity chestWorkout = testServiceUtils.createChestWorkout();

        when(trainingProgramRepository.findById(1L)).thenReturn(Optional.ofNullable(chestWorkout));

        TrainingProgramDetailsViewModel trainingProgram = serviceToTest.getTrainingProgram(1);

        assertEquals("Chest Workout", trainingProgram.getTitle());
        assertEquals("Georgi Georgiev", trainingProgram.getAuthor());
        assertEquals(GoalEnum.GAIN_MASS, trainingProgram.getGoal());
        assertEquals(BodyPartEnum.CHEST, trainingProgram.getBodyParts().get(0));
    }

    @Test
    void testCanModify_ReturnsTrueForOwnTraining() {
        TrainingProgramEntity chestWorkout = testServiceUtils.createChestWorkout();

        when(userRepository.findByEmail("georgi@abv.bg")).thenReturn(Optional.ofNullable(testServiceUtils.createUser()));
        when(trainingProgramRepository.findById(1L)).thenReturn(Optional.ofNullable(chestWorkout));

        boolean canModify = serviceToTest.canModify("georgi@abv.bg", 1);

        assertTrue(canModify);
    }

    @Test
    void testCanModify_ReturnsFalseForOthersTraining() {
        TrainingProgramEntity absWorkout = testServiceUtils.createAbsWorkout();

        when(userRepository.findByEmail("georgi@abv.bg")).thenReturn(Optional.ofNullable(testServiceUtils.createUser()));
        when(trainingProgramRepository.findById(2L)).thenReturn(Optional.ofNullable(absWorkout));

        boolean canModify = serviceToTest.canModify("georgi@abv.bg", 2);

        assertFalse(canModify);
    }

    @Test
    void testCanModify_ReturnsTrueForAdmin() {
        TrainingProgramEntity absWorkout = testServiceUtils.createAbsWorkout();

        when(userRepository.findByEmail("admin@abv.bg")).thenReturn(Optional.ofNullable(testServiceUtils.createAdmin()));
        when(trainingProgramRepository.findById(2L)).thenReturn(Optional.ofNullable(absWorkout));

        boolean canModify = serviceToTest.canModify("admin@abv.bg", 2);

        assertTrue(canModify);
    }

    @Test
    void testIsInUserFavourites_ReturnsTrueWhenInUserFavourites() {
        UserEntity user = testServiceUtils.createUser();
        TrainingProgramEntity absWorkout = testServiceUtils.createAbsWorkout();
        user.addFavouriteTrainingProgram(absWorkout);

        when(userRepository.findByEmail("georgi@abv.bg")).thenReturn(Optional.of(user));
        when(trainingProgramRepository.findById(2L)).thenReturn(Optional.ofNullable(absWorkout));

        boolean inUserFavourites = serviceToTest.isInUserFavourites("georgi@abv.bg", 2);

        assertTrue(inUserFavourites);
    }

    @Test
    void testIsInUserFavourites_ReturnsFalseWhenNotInUserFavourites() {
        TrainingProgramEntity absWorkout = testServiceUtils.createAbsWorkout();

        when(userRepository.findByEmail("georgi@abv.bg")).thenReturn(Optional.ofNullable(testServiceUtils.createUser()));
        when(trainingProgramRepository.findById(2L)).thenReturn(Optional.ofNullable(absWorkout));

        boolean inUserFavourites = serviceToTest.isInUserFavourites("georgi@abv.bg", 2);

        assertFalse(inUserFavourites);
    }

    @Test
    void testDeleteProgram() {
        serviceToTest.deleteProgram(1);
        verify(trainingProgramRepository, times(1)).deleteById(1L);
    }

    @Test
    void testUpdateProgram() {
        TrainingProgramServiceModel serviceModel = testServiceUtils.createTrainingProgramServiceModel();
        BodyPartEntity abs = new BodyPartEntity().setName(BodyPartEnum.ABS);

        when(bodyPartRepository.findByName(BodyPartEnum.ABS)).thenReturn(Optional.ofNullable(abs));
        when(userRepository.findByEmail("pesho@abv.bg"))
                .thenReturn(Optional.ofNullable(testServiceUtils.createAnotherUser()));
        when(goalRepository.findByName(GoalEnum.LOSE_FAT)).thenReturn(Optional.ofNullable(testServiceUtils.createLoseFatGoal()));

        serviceToTest.updateProgram(serviceModel);

        verify(trainingProgramRepository, times(1)).save(any());
    }

    @Test
    void testAddToFavourites() {
        TrainingProgramEntity absWorkout = testServiceUtils.createAbsWorkout();

        when(userRepository.findByEmail("georgi@abv.bg")).thenReturn(Optional.ofNullable(testServiceUtils.createUser()));
        when(trainingProgramRepository.findById(2L)).thenReturn(Optional.ofNullable(absWorkout));

        serviceToTest.addToFavourites(2, "georgi@abv.bg");

        verify(userRepository, times(1)).save(any());
    }

    @Test
    void testRemoveFromFavourites() {
        UserEntity user = testServiceUtils.createUser();
        TrainingProgramEntity absWorkout = testServiceUtils.createAbsWorkout();
        user.addFavouriteTrainingProgram(absWorkout);

        when(userRepository.findByEmail("georgi@abv.bg")).thenReturn(Optional.of(user));
        when(trainingProgramRepository.findById(2L)).thenReturn(Optional.ofNullable(absWorkout));

        serviceToTest.removeFromFavourites(2, "georgi@abv.bg");

        verify(userRepository, times(1)).save(any());
    }

    @Test
    void testAddComment() {
        UserEntity user = testServiceUtils.createUser();
        TrainingProgramEntity absWorkout = testServiceUtils.createAbsWorkout();
        CommentAddServiceModel commentServiceModel = testServiceUtils.createCommentServiceModel();
        CommentEntity comment = new CommentEntity();

        when(userRepository.findByEmail("georgi@abv.bg")).thenReturn(Optional.of(user));
        when(trainingProgramRepository.findById(2L)).thenReturn(Optional.ofNullable(absWorkout));
        when(modelMapper.map(commentServiceModel, CommentEntity.class)).thenReturn(comment);

        serviceToTest.addComment(commentServiceModel, 2);

        verify(commentRepository, times(1)).save(comment);
    }

    @Test
    void testGetComments() {
        List<CommentEntity> comments = testServiceUtils.createComments();
        TrainingProgramEntity absWorkout = testServiceUtils.createAbsWorkout()
                .setComments(comments);

        when(trainingProgramRepository.findById(2L)).thenReturn(Optional.ofNullable(absWorkout));
        when(commentRepository.findAllByTrainingProgramAndApproved(absWorkout, true)).thenReturn(comments);

        List<CommentViewModel> result = serviceToTest.getComments(2);

        assertEquals(2, result.size());
        assertEquals("Wow!", result.get(0).getTextContent());
        assertEquals("Amazing!", result.get(1).getTextContent());
    }

    @Test
    void testGetLastPageNumberReturnsOnePageWhenRecordsAreLessThanOrEqualToSize() {
        when(trainingProgramRepository.count()).thenReturn(3L);

        int lastPageNumber = serviceToTest.getLastPageNumber(null, null, 3);

        assertEquals(1, lastPageNumber);
    }

    @Test
    void testGetLastPageNumberReturnsTwoPagesWhenRecordsAreGreaterThanPageSize() {
        when(trainingProgramRepository.count()).thenReturn(4L);

        int lastPageNumber = serviceToTest.getLastPageNumber(null, null, 3);

        assertEquals(2, lastPageNumber);
    }

    @Test
    void removeExerciseFromTraining() {
        TrainingProgramEntity absWorkout = testServiceUtils.createAbsWorkout();

        when(trainingProgramRepository.findById(2L)).thenReturn(Optional.ofNullable(absWorkout));

        serviceToTest.removeExerciseFromTraining(2, "Plank");

        verify(trainingProgramRepository, times(1)).save(absWorkout);
    }
}
