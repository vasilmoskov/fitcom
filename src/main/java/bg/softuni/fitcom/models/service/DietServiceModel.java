package bg.softuni.fitcom.models.service;

import bg.softuni.fitcom.models.enums.GoalEnum;

public class DietServiceModel {
    private long id;
    private String title;
    private String description;
    private String author;
    private GoalEnum goal;

    public String getTitle() {
        return title;
    }

    public DietServiceModel setTitle(String title) {
        this.title = title;
        return this;
    }

    public String getDescription() {
        return description;
    }

    public DietServiceModel setDescription(String description) {
        this.description = description;
        return this;
    }

    public String getAuthor() {
        return author;
    }

    public DietServiceModel setAuthor(String author) {
        this.author = author;
        return this;
    }

    public GoalEnum getGoal() {
        return goal;
    }

    public DietServiceModel setGoal(GoalEnum goal) {
        this.goal = goal;
        return this;
    }

    public long getId() {
        return id;
    }

    public DietServiceModel setId(long id) {
        this.id = id;
        return this;
    }
}
