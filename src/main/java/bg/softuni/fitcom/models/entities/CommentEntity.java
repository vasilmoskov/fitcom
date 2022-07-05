package bg.softuni.fitcom.models.entities;

import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import java.time.LocalDateTime;

@Entity
@Table(name = "comments")
public class CommentEntity extends BaseEntity {
    @Column
    private boolean approved;

    @Column
    private LocalDateTime created;

    @Column
    private String textContent;

    @ManyToOne
    private UserEntity author;

    @ManyToOne // (cascade = CascadeType.REMOVE)
//    @OnDelete(action = OnDeleteAction.CASCADE)
    private TrainingProgramEntity trainingProgram;

    @ManyToOne
    private DietEntity diet;

    public boolean getApproved() {
        return approved;
    }

    public CommentEntity setApproved(boolean approved) {
        this.approved = approved;
        return this;
    }

    public LocalDateTime getCreated() {
        return created;
    }

    public CommentEntity setCreated(LocalDateTime created) {
        this.created = created;
        return this;
    }

    public String getTextContent() {
        return textContent;
    }

    public CommentEntity setTextContent(String textContent) {
        this.textContent = textContent;
        return this;
    }

    public UserEntity getAuthor() {
        return author;
    }

    public CommentEntity setAuthor(UserEntity author) {
        this.author = author;
        return this;
    }

    public TrainingProgramEntity getTrainingProgram() {
        return trainingProgram;
    }

    public CommentEntity setTrainingProgram(TrainingProgramEntity trainingProgram) {
        this.trainingProgram = trainingProgram;
        return this;
    }

    public DietEntity getDiet() {
        return diet;
    }

    public CommentEntity setDiet(DietEntity diet) {
        this.diet = diet;
        return this;
    }
}
