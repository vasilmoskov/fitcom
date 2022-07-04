package bg.softuni.fitcom.models.entities;

import bg.softuni.fitcom.models.enums.BodyPartEnum;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.Lob;
import javax.persistence.ManyToMany;
import javax.persistence.Table;
import java.util.List;

@Entity
@Table(name = "exercises")
public class ExerciseEntity extends BaseEntity {
    @Column(nullable = false, unique = true)
    private String name;

    @Lob
    @Column(nullable = false)
    private String description;

    @Column(nullable = false)
    private String videoUrl;

    public String getName() {
        return name;
    }

    public ExerciseEntity setName(String name) {
        this.name = name;
        return this;
    }

    public String getDescription() {
        return description;
    }

    public ExerciseEntity setDescription(String description) {
        this.description = description;
        return this;
    }

    public String getVideoUrl() {
        return videoUrl;
    }

    public ExerciseEntity setVideoUrl(String videoUrl) {
        this.videoUrl = videoUrl;
        return this;
    }


}
