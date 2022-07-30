package bg.softuni.fitcom.services;

import bg.softuni.fitcom.exceptions.ResourceNotFoundException;
import bg.softuni.fitcom.models.entities.ExerciseEntity;
import bg.softuni.fitcom.models.view.ExerciseDetailsViewModel;
import bg.softuni.fitcom.repositories.ExerciseRepository;
import bg.softuni.fitcom.services.impl.ExerciseServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class ExerciseServiceTest {

    @Mock
    private ExerciseRepository exerciseRepository;

    @Mock
    private ModelMapper modelMapper;

    private ExerciseService serviceToTest;

    @BeforeEach
    void setup() {
        serviceToTest = new ExerciseServiceImpl(exerciseRepository, modelMapper);
    }

    @Test
    void testGetExercise_shouldReturnExercise() {
        ExerciseEntity exercise = createExercise();
        ExerciseDetailsViewModel expected = createExerciseView();

        when(exerciseRepository.findById(1L)).thenReturn(Optional.of(exercise));
        when(modelMapper.map(exercise, ExerciseDetailsViewModel.class)).thenReturn(expected);

        ExerciseDetailsViewModel actual = serviceToTest.getExercise(1);

        assertEquals(expected.getName(), actual.getName());
        assertEquals(expected.getDescription(), actual.getDescription());
    }

    @Test
    void testGetExercise_throwsNotFound() {
        when(exerciseRepository.findById(2L)).thenThrow(ResourceNotFoundException.class);

        assertThrows(ResourceNotFoundException.class, () -> serviceToTest.getExercise(2));
    }

    private ExerciseDetailsViewModel createExerciseView() {
        return new ExerciseDetailsViewModel()
                .setName("Bench press")
                .setDescription("Do it slowly");
    }

    private ExerciseEntity createExercise() {
        ExerciseEntity exercise = new ExerciseEntity()
                .setName("Bench press")
                .setDescription("Do it slowly");

        exercise.setId(1);

        return exercise;
    }
}
