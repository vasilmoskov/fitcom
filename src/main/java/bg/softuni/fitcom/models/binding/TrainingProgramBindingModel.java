package bg.softuni.fitcom.models.binding;

import bg.softuni.fitcom.models.enums.BodyPartEnum;
import bg.softuni.fitcom.models.enums.GoalEnum;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.List;

public class TrainingProgramBindingModel {

    private long id;

    @NotBlank(message = "Please give your training a title.")
    private String title;

    @NotBlank(message = "Your training should have a description.")
    private String description;

    @NotNull(message = "Please select the goal of your training.")
    private GoalEnum goal;

    List<BodyPartEnum> bodyParts;

    @NotNull(message = "Please add exercises in your training.")
    private List<String> exercisesData;


    public long getId() {
        return id;
    }

    public TrainingProgramBindingModel setId(long id) {
        this.id = id;
        return this;
    }

    public String getTitle() {
        return title;
    }

    public TrainingProgramBindingModel setTitle(String title) {
        this.title = title;
        return this;
    }

    public String getDescription() {
        return description;
    }

    public TrainingProgramBindingModel setDescription(String description) {
        this.description = description;
        return this;
    }

    public GoalEnum getGoal() {
        return goal;
    }

    public TrainingProgramBindingModel setGoal(GoalEnum goal) {
        this.goal = goal;
        return this;
    }

    public List<BodyPartEnum> getBodyParts() {
        return bodyParts;
    }

    public TrainingProgramBindingModel setBodyParts(List<BodyPartEnum> bodyParts) {
        this.bodyParts = bodyParts;
        return this;
    }

    public List<String> getExercisesData() {
        return exercisesData;
    }

    public TrainingProgramBindingModel setExercisesData(List<String> exercisesData) {
        this.exercisesData = exercisesData;
        return this;
    }
}
