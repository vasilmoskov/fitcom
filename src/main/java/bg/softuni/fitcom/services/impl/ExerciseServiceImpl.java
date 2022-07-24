package bg.softuni.fitcom.services.impl;

import bg.softuni.fitcom.exceptions.ResourceNotFoundException;
import bg.softuni.fitcom.models.entities.ExerciseEntity;
import bg.softuni.fitcom.models.view.ExerciseDetailsViewModel;
import bg.softuni.fitcom.repositories.ExerciseRepository;
import bg.softuni.fitcom.services.ExerciseService;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

@Service
public class ExerciseServiceImpl implements ExerciseService {
    private final ExerciseRepository exerciseRepository;
    private final ModelMapper modelMapper;

    public ExerciseServiceImpl(ExerciseRepository exerciseRepository, ModelMapper modelMapper) {
        this.exerciseRepository = exerciseRepository;
        this.modelMapper = modelMapper;
    }

    @Override
    public ExerciseDetailsViewModel getExercise(long id) {
        ExerciseEntity entity = this.exerciseRepository
                .findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Exercise with id: " + id + " does not exist!"));

        return this.modelMapper.map(entity, ExerciseDetailsViewModel.class);
    }
}
