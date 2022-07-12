package bg.softuni.fitcom.web.controllers;

import bg.softuni.fitcom.models.view.ExerciseDetailsViewModel;
import bg.softuni.fitcom.services.ExerciseService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@Controller
public class ExerciseController {
    private final ExerciseService exerciseService;

    public ExerciseController(ExerciseService exerciseService) {
        this.exerciseService = exerciseService;
    }

    @GetMapping("/exercises/{id}")
    public String getExercise(@PathVariable long id, Model model) {
        model.addAttribute("exercise", this.exerciseService.getExercise(id));
        return "exercise-details";
    }
}
