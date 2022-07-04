package bg.softuni.fitcom.models.entities;

import bg.softuni.fitcom.models.enums.PurposeEnum;

import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Table;

@Entity
@Table(name = "purposes")
public class PurposeEntity extends BaseEntity {
    @Enumerated(value = EnumType.STRING)
    private PurposeEnum name;

    public PurposeEnum getName() {
        return name;
    }

    public PurposeEntity setName(PurposeEnum name) {
        this.name = name;
        return this;
    }
}
