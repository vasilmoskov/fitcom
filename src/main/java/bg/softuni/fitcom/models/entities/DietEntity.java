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
@Table(name = "diets")
public class DietEntity extends BaseEntity {
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

    @OneToMany(mappedBy = "diet", cascade = CascadeType.REMOVE)
    private List<CommentEntity> comments;

    @ManyToMany(cascade =
            {
                    CascadeType.DETACH,
                    CascadeType.MERGE,
                    CascadeType.REFRESH,
                    CascadeType.PERSIST
            },
            targetEntity = UserEntity.class)
    @JoinTable(name = "users_favourite_diets",
            inverseJoinColumns = @JoinColumn(name = "user_id", referencedColumnName = "id"),
            joinColumns = @JoinColumn(name = "diet_id", referencedColumnName = "id"),
            foreignKey = @ForeignKey(ConstraintMode.CONSTRAINT),
            inverseForeignKey = @ForeignKey(ConstraintMode.CONSTRAINT))
    private List<UserEntity> usersFavourites;

    public String getTitle() {
        return title;
    }

    public DietEntity setTitle(String title) {
        this.title = title;
        return this;
    }

    public String getDescription() {
        return description;
    }

    public DietEntity setDescription(String description) {
        this.description = description;
        return this;
    }

    public UserEntity getAuthor() {
        return author;
    }

    public DietEntity setAuthor(UserEntity author) {
        this.author = author;
        return this;
    }

    public GoalEntity getGoal() {
        return goal;
    }

    public DietEntity setGoal(GoalEntity category) {
        this.goal = category;
        return this;
    }

    public LocalDateTime getCreated() {
        return created;
    }

    public DietEntity setCreated(LocalDateTime created) {
        this.created = created;
        return this;
    }

    public List<CommentEntity> getComments() {
        return comments;
    }

    public DietEntity setComments(List<CommentEntity> comments) {
        this.comments = comments;
        return this;
    }

    public List<UserEntity> getUsersFavourites() {
        return usersFavourites;
    }

    public DietEntity setUsersFavourites(List<UserEntity> usersFavourites) {
        this.usersFavourites = usersFavourites;
        return this;
    }
}
