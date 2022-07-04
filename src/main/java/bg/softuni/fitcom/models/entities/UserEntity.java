package bg.softuni.fitcom.models.entities;

import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.Table;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "users")
public class UserEntity extends BaseEntity {
    @Column
    private String firstName;

    @Column
    private String lastName;

    @Column
    private Integer age;

    @Column(nullable = false, unique = true)
    private String email;

    @Column(nullable = false)
    private String password;

    @Column
    private String pictureUrl;

    @Column
    private String picturePublicId;

    @ManyToMany
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JoinTable(name = "users_roles",
            joinColumns = @JoinColumn(name = "user_id", referencedColumnName = "id"),
            inverseJoinColumns = @JoinColumn(name = "role_id", referencedColumnName = "id"))
    private List<RoleEntity> roles;

    @ManyToMany
    @JoinTable(name = "users_pending_roles",
            joinColumns = @JoinColumn(name = "user_id", referencedColumnName = "id"),
            inverseJoinColumns = @JoinColumn(name = "role_id", referencedColumnName = "id"))
    private List<RoleEntity> pendingRoles = new ArrayList<>();

    @ManyToMany //(cascade = CascadeType.REMOVE)
//    @OnDelete(action = OnDeleteAction.CASCADE)
    @JoinTable(name = "users_favourite_training_programs",
            joinColumns = @JoinColumn(name = "user_id", referencedColumnName = "id"),
            inverseJoinColumns = @JoinColumn(name = "training_program_id", referencedColumnName = "id"))
    private List<TrainingProgramEntity> favouriteTrainingPrograms = new ArrayList<>();

    @ManyToMany
    @JoinTable(name = "users_favourite_diets",
            joinColumns = @JoinColumn(name = "user_id", referencedColumnName = "id"),
            inverseJoinColumns = @JoinColumn(name = "diet_id", referencedColumnName = "id"))
    private List<DietEntity> favouriteDiets = new ArrayList<>();

    public String getFirstName() {
        return firstName;
    }

    public UserEntity setFirstName(String firstName) {
        this.firstName = firstName;
        return this;
    }

    public String getLastName() {
        return lastName;
    }

    public UserEntity setLastName(String lastName) {
        this.lastName = lastName;
        return this;
    }

    public Integer getAge() {
        return age;
    }

    public UserEntity setAge(Integer age) {
        this.age = age;
        return this;
    }

    public String getEmail() {
        return email;
    }

    public UserEntity setEmail(String email) {
        this.email = email;
        return this;
    }

    public String getPassword() {
        return password;
    }

    public UserEntity setPassword(String password) {
        this.password = password;
        return this;
    }

    public List<RoleEntity> getRoles() {
        return roles;
    }

    public UserEntity setRoles(List<RoleEntity> roles) {
        this.roles = roles;
        return this;
    }

    public UserEntity addRole(RoleEntity role) {
        this.roles.add(role);
        return this;
    }

    public List<TrainingProgramEntity> getFavouriteTrainingPrograms() {
        return favouriteTrainingPrograms;
    }

    public UserEntity setFavouriteTrainingPrograms(List<TrainingProgramEntity> favouriteTrainingPrograms) {
        this.favouriteTrainingPrograms = favouriteTrainingPrograms;
        return this;
    }

    public UserEntity addFavouriteTrainingProgram(TrainingProgramEntity trainingProgram) {
        this.favouriteTrainingPrograms.add(trainingProgram);
        return this;
    }

    public List<DietEntity> getFavouriteDiets() {
        return favouriteDiets;
    }

    public UserEntity setFavouriteDiets(List<DietEntity> favouriteDiets) {
        this.favouriteDiets = favouriteDiets;
        return this;
    }

    public UserEntity addFavouriteDiet(DietEntity dietEntity) {
        this.favouriteDiets.add(dietEntity);
        return this;
    }

    public List<RoleEntity> getPendingRoles() {
        return pendingRoles;
    }

    public UserEntity setPendingRoles(List<RoleEntity> pendingRoles) {
        this.pendingRoles = pendingRoles;
        return this;
    }

    public UserEntity addPendingRole(RoleEntity role) {
        this.pendingRoles.add(role);
        return this;
    }

    public String getPictureUrl() {
        return pictureUrl;
    }

    public UserEntity setPictureUrl(String pictureUrl) {
        this.pictureUrl = pictureUrl;
        return this;
    }

    public String getPicturePublicId() {
        return picturePublicId;
    }

    public UserEntity setPicturePublicId(String picturePublicId) {
        this.picturePublicId = picturePublicId;
        return this;
    }
}
