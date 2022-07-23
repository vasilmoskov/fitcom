package bg.softuni.fitcom.models.entities;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import java.time.LocalDateTime;

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
}
