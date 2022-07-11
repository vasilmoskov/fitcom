package bg.softuni.fitcom.models.view;

import bg.softuni.fitcom.models.entities.UserEntity;
import bg.softuni.fitcom.models.enums.PurposeEnum;

public class DietDetailsViewModel {
    private long id;
    private String title;
    private String description;
    private String created;
    private String author;
    private PurposeEnum purpose;

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

    public PurposeEnum getPurpose() {
        return purpose;
    }

    public DietDetailsViewModel setPurpose(PurposeEnum purpose) {
        this.purpose = purpose;
        return this;
    }
}
