package bg.softuni.fitcom.models.entities;

import bg.softuni.fitcom.models.enums.GoalEnum;

import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Table;

@Entity
@Table(name = "goals")
public class GoalEntity extends BaseEntity {
    @Enumerated(value = EnumType.STRING)
    private GoalEnum name;

    public GoalEnum getName() {
        return name;
    }

    public GoalEntity setName(GoalEnum name) {
        this.name = name;
        return this;
    }
}
