package bg.softuni.fitcom.services;

import bg.softuni.fitcom.models.view.ExerciseDetailsViewModel;

public interface ExerciseService {

    ExerciseDetailsViewModel getExercise(long id);
}
