package bg.softuni.fitcom.models.entities;

import bg.softuni.fitcom.models.enums.BodyPartEnum;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Table;

@Entity
@Table(name = "body_parts")
public class BodyPartEntity extends BaseEntity {
    @Enumerated(value = EnumType.STRING)
    private BodyPartEnum name;

    @Column
    private String pictureUrl;

    public BodyPartEnum getName() {
        return name;
    }

    public BodyPartEntity setName(BodyPartEnum name) {
        this.name = name;
        return this;
    }

    public String getPictureUrl() {
        return pictureUrl;
    }

    public BodyPartEntity setPictureUrl(String pictureUrl) {
        this.pictureUrl = pictureUrl;
        return this;
    }
}
