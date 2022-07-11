package bg.softuni.fitcom.models.service;

import bg.softuni.fitcom.models.entities.UserEntity;

import java.time.LocalDateTime;

public class CommentAddServiceModel {
    private LocalDateTime created;
    private Boolean approved; // TODO: pending comments for admins - don't show until approved
    private String textContent;
    private String author;

    public LocalDateTime getCreated() {
        return created;
    }

    public CommentAddServiceModel setCreated(LocalDateTime created) {
        this.created = created;
        return this;
    }

    public String getTextContent() {
        return textContent;
    }

    public CommentAddServiceModel setTextContent(String textContent) {
        this.textContent = textContent;
        return this;
    }

    public String getAuthor() {
        return author;
    }

    public CommentAddServiceModel setAuthor(String author) {
        this.author = author;
        return this;
    }

    public Boolean getApproved() {
        return approved;
    }

    public CommentAddServiceModel setApproved(Boolean approved) {
        this.approved = approved;
        return this;
    }
}
