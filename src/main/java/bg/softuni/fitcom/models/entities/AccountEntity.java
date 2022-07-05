package bg.softuni.fitcom.models.entities;

import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.Table;
import java.util.List;

@Entity
@Table(name = "accounts")
public class AccountEntity extends BaseEntity {
    @Column
    private String firstName;

    @Column
    private String lastName;

    @Column
    private Integer age;

    @Column(nullable = false)
    private String email;

    @Column(nullable = false)
    private String password;

    @Column
    private String pictureUrl;

    @Column
    private String picturePublicId;

    @ManyToMany
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JoinTable(name = "accounts_roles",
            joinColumns = @JoinColumn(name = "account_id", referencedColumnName = "id"),
            inverseJoinColumns = @JoinColumn(name = "role_id", referencedColumnName = "id"))
    private List<RoleEntity> roles;

    public String getFirstName() {
        return firstName;
    }

    public AccountEntity setFirstName(String firstName) {
        this.firstName = firstName;
        return this;
    }

    public String getLastName() {
        return lastName;
    }

    public AccountEntity setLastName(String lastName) {
        this.lastName = lastName;
        return this;
    }

    public Integer getAge() {
        return age;
    }

    public AccountEntity setAge(Integer age) {
        this.age = age;
        return this;
    }

    public String getEmail() {
        return email;
    }

    public AccountEntity setEmail(String email) {
        this.email = email;
        return this;
    }

    public String getPassword() {
        return password;
    }

    public AccountEntity setPassword(String password) {
        this.password = password;
        return this;
    }

    public List<RoleEntity> getRoles() {
        return roles;
    }

    public AccountEntity setRoles(List<RoleEntity> roles) {
        this.roles = roles;
        return this;
    }

    public String getPictureUrl() {
        return pictureUrl;
    }

    public AccountEntity setPictureUrl(String pictureUrl) {
        this.pictureUrl = pictureUrl;
        return this;
    }

    public String getPicturePublicId() {
        return picturePublicId;
    }

    public AccountEntity setPicturePublicId(String picturePublicId) {
        this.picturePublicId = picturePublicId;
        return this;
    }
}
