package bg.softuni.fitcom.models.view;

import bg.softuni.fitcom.models.enums.BodyPartEnum;
import bg.softuni.fitcom.models.enums.GoalEnum;

import java.util.List;

public class TrainingProgramDetailsViewModel {
    private long id;
    private String title;
    private String description;
    private String author;
    private boolean canModify;
    private String created;
    private GoalEnum goal;
    private String pictureUrl;
    private List<BodyPartEnum> bodyParts;
    private List<ExerciseOverviewViewModel> exercises;

    public TrainingProgramDetailsViewModel() {
    }

    public long getId() {
        return id;
    }

    public TrainingProgramDetailsViewModel setId(long id) {
        this.id = id;
        return this;
    }

    public String getTitle() {
        return title;
    }

    public TrainingProgramDetailsViewModel setTitle(String title) {
        this.title = title;
        return this;
    }

    public String getDescription() {
        return description;
    }

    public TrainingProgramDetailsViewModel setDescription(String description) {
        this.description = description;
        return this;
    }

    public String getAuthor() {
        return author;
    }

    public TrainingProgramDetailsViewModel setAuthor(String author) {
        this.author = author;
        return this;
    }

    public boolean isCanModify() {
        return canModify;
    }

    public TrainingProgramDetailsViewModel setCanModify(boolean canModify) {
        this.canModify = canModify;
        return this;
    }

    public String getCreated() {
        return created;
    }

    public TrainingProgramDetailsViewModel setCreated(String created) {
        this.created = created;
        return this;
    }

    public List<BodyPartEnum> getBodyParts() {
        return bodyParts;
    }

    public TrainingProgramDetailsViewModel setBodyParts(List<BodyPartEnum> bodyParts) {
        this.bodyParts = bodyParts;
        return this;
    }

    public List<ExerciseOverviewViewModel> getExercises() {
        return exercises;
    }

    public TrainingProgramDetailsViewModel setExercises(List<ExerciseOverviewViewModel> exercises) {
        this.exercises = exercises;
        return this;
    }

    public GoalEnum getGoal() {
        return goal;
    }

    public TrainingProgramDetailsViewModel setGoal(GoalEnum goal) {
        this.goal = goal;
        return this;
    }

    public String getPictureUrl() {
        return pictureUrl;
    }

    public TrainingProgramDetailsViewModel setPictureUrl(String pictureUrl) {
        this.pictureUrl = pictureUrl;
        return this;
    }
}
