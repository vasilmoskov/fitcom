package bg.softuni.fitcom.models.view;

import bg.softuni.fitcom.models.enums.GoalEnum;

public class DietDetailsViewModel {
    private long id;
    private String title;
    private String description;
    private String created;
    private String author;
    private GoalEnum goal;
    private String pictureUrl;

    public String getTitle() {
        return title;
    }

    public DietDetailsViewModel setTitle(String title) {
        this.title = title;
        return this;
    }

    public String getDescription() {
        return description;
    }

    public DietDetailsViewModel setDescription(String description) {
        this.description = description;
        return this;
    }

    public String getCreated() {
        return created;
    }

    public DietDetailsViewModel setCreated(String created) {
        this.created = created;
        return this;
    }

    public String getAuthor() {
        return author;
    }

    public DietDetailsViewModel setAuthor(String author) {
        this.author = author;
        return this;
    }

    public long getId() {
        return id;
    }

    public DietDetailsViewModel setId(long id) {
        this.id = id;
        return this;
    }

    public GoalEnum getGoal() {
        return goal;
    }

    public DietDetailsViewModel setGoal(GoalEnum goal) {
        this.goal = goal;
        return this;
    }

    public String getPictureUrl() {
        return pictureUrl;
    }

    public DietDetailsViewModel setPictureUrl(String pictureUrl) {
        this.pictureUrl = pictureUrl;
        return this;
    }
}
