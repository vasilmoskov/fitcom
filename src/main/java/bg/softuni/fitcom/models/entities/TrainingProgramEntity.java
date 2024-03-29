package bg.softuni.fitcom.models.entities;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.ConstraintMode;
import javax.persistence.Entity;
import javax.persistence.ForeignKey;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.Lob;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "training_programs")
public class TrainingProgramEntity extends BaseEntity {
    @Column
    private String title;

    @Lob
    private String description;

    @Column
    private LocalDateTime created;

    @ManyToOne
    private UserEntity author;

    @ManyToOne
    private GoalEntity goal;

    @ManyToMany
    @JoinTable(name = "training_programs_body_parts",
            joinColumns = @JoinColumn(name = "training_program_id", referencedColumnName = "id"),
            inverseJoinColumns = @JoinColumn(name = "body_part_id", referencedColumnName = "id"))
    private List<BodyPartEntity> bodyParts;

    @ManyToMany(cascade = CascadeType.ALL)
    @JoinTable(name = "training_programs_exercises",
            joinColumns = @JoinColumn(name = "training_program_id", referencedColumnName = "id"),
            inverseJoinColumns = @JoinColumn(name = "exercise_id", referencedColumnName = "id"))
    private List<ExerciseEntity> exercises;

    @OneToMany(mappedBy = "trainingProgram", cascade = CascadeType.REMOVE)
    private List<CommentEntity> comments;

    @ManyToMany(cascade =
            {
                    CascadeType.DETACH,
                    CascadeType.MERGE,
                    CascadeType.REFRESH,
                    CascadeType.PERSIST
            },
            targetEntity = UserEntity.class)
    @JoinTable(name = "users_favourite_training_programs",
            inverseJoinColumns = @JoinColumn(name = "user_id", referencedColumnName = "id"),
            joinColumns = @JoinColumn(name = "training_program_id", referencedColumnName = "id"),
            foreignKey = @ForeignKey(ConstraintMode.CONSTRAINT),
            inverseForeignKey = @ForeignKey(ConstraintMode.CONSTRAINT))
    private List<UserEntity> usersFavouriteTrainings;

    public String getDescription() {
        return description;
    }

    public TrainingProgramEntity setDescription(String description) {
        this.description = description;
        return this;
    }

    public String getTitle() {
        return title;
    }

    public TrainingProgramEntity setTitle(String title) {
        this.title = title;
        return this;
    }

    public UserEntity getAuthor() {
        return author;
    }

    public TrainingProgramEntity setAuthor(UserEntity author) {
        this.author = author;
        return this;
    }

    public GoalEntity getGoal() {
        return goal;
    }

    public TrainingProgramEntity setGoal(GoalEntity category) {
        this.goal = category;
        return this;
    }

    public List<BodyPartEntity> getBodyParts() {
        return bodyParts;
    }

    public TrainingProgramEntity setBodyParts(List<BodyPartEntity> bodyParts) {
        this.bodyParts = bodyParts;
        return this;
    }

    public List<ExerciseEntity> getExercises() {
        return exercises;
    }

    public TrainingProgramEntity setExercises(List<ExerciseEntity> exercises) {
        this.exercises = exercises;
        return this;
    }

    public TrainingProgramEntity addExercise(ExerciseEntity exercise) {
        exercises.add(exercise);
        return this;
    }

    public LocalDateTime getCreated() {
        return created;
    }

    public TrainingProgramEntity setCreated(LocalDateTime created) {
        this.created = created;
        return this;
    }

    public List<CommentEntity> getComments() {
        return comments;
    }

    public TrainingProgramEntity setComments(List<CommentEntity> comments) {
        this.comments = comments;
        return this;
    }

    public List<UserEntity> getUsersFavouriteTrainings() {
        return usersFavouriteTrainings;
    }

    public TrainingProgramEntity setUsersFavouriteTrainings(List<UserEntity> usersFavouriteTrainings) {
        this.usersFavouriteTrainings = usersFavouriteTrainings;
        return this;
    }
}
