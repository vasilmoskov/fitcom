package bg.softuni.fitcom.models.entities;

import bg.softuni.fitcom.models.enums.RoleEnum;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.ManyToMany;
import javax.persistence.Table;
import java.util.List;

@Entity
@Table(name = "roles")
public class RoleEntity extends BaseEntity {
    @Column(name = "name")
    @Enumerated(value = EnumType.STRING)
    private RoleEnum role;

    @ManyToMany(mappedBy = "pendingRoles")
    private List<UserEntity> pendingUsers;

    public List<UserEntity> getPendingUsers() {
        return pendingUsers;
    }

    public RoleEntity setPendingUsers(List<UserEntity> users) {
        this.pendingUsers = users;
        return this;
    }

    public RoleEnum getRole() {
        return role;
    }

    public RoleEntity setRole(RoleEnum role) {
        this.role = role;
        return this;
    }
}
