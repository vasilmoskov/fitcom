package bg.softuni.fitcom.models.service;

import bg.softuni.fitcom.models.enums.BodyPartEnum;
import bg.softuni.fitcom.models.enums.GoalEnum;

import java.util.List;

public class TrainingProgramServiceModel {
    private long id;
    private String title;
    private String description;
    private String author;
    private GoalEnum goal;
    private List<BodyPartEnum> bodyParts;
    private List<ExerciseAddServiceModel> exercises;

    public long getId() {
        return id;
    }

    public TrainingProgramServiceModel setId(long id) {
        this.id = id;
        return this;
    }

    public String getTitle() {
        return title;
    }

    public TrainingProgramServiceModel setTitle(String title) {
        this.title = title;
        return this;
    }

    public String getDescription() {
        return description;
    }

    public TrainingProgramServiceModel setDescription(String description) {
        this.description = description;
        return this;
    }

    public String getAuthor() {
        return author;
    }

    public TrainingProgramServiceModel setAuthor(String author) {
        this.author = author;
        return this;
    }

    public GoalEnum getGoal() {
        return goal;
    }

    public TrainingProgramServiceModel setGoal(GoalEnum goal) {
        this.goal = goal;
        return this;
    }

    public List<BodyPartEnum> getBodyParts() {
        return bodyParts;
    }

    public TrainingProgramServiceModel setBodyParts(List<BodyPartEnum> bodyParts) {
        this.bodyParts = bodyParts;
        return this;
    }

    public List<ExerciseAddServiceModel> getExercises() {
        return exercises;
    }

    public TrainingProgramServiceModel setExercises(List<ExerciseAddServiceModel> exercises) {
        this.exercises = exercises;
        return this;
    }
}
