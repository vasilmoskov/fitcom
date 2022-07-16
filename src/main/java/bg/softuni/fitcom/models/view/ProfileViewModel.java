package bg.softuni.fitcom.models.view;

import bg.softuni.fitcom.models.enums.RoleEnum;

import java.util.List;

public class ProfileViewModel {
    private long id;
    private String firstName;
    private String lastName;
    private String email;
    private Integer age;
    private String pictureUrl;
    private String picturePublicId;
    private List<RoleEnum> roles;

    public long getId() {
        return id;
    }

    public ProfileViewModel setId(long id) {
        this.id = id;
        return this;
    }

    public String getFirstName() {
        return firstName;
    }

    public ProfileViewModel setFirstName(String firstName) {
        this.firstName = firstName;
        return this;
    }

    public String getLastName() {
        return lastName;
    }

    public ProfileViewModel setLastName(String lastName) {
        this.lastName = lastName;
        return this;
    }

    public String getEmail() {
        return email;
    }

    public ProfileViewModel setEmail(String email) {
        this.email = email;
        return this;
    }

    public Integer getAge() {
        return age;
    }

    public ProfileViewModel setAge(Integer age) {
        this.age = age;
        return this;
    }

    public List<RoleEnum> getRoles() {
        return roles;
    }

    public ProfileViewModel setRoles(List<RoleEnum> roles) {
        this.roles = roles;
        return this;
    }

    public String getPictureUrl() {
        return pictureUrl;
    }

    public ProfileViewModel setPictureUrl(String pictureUrl) {
        this.pictureUrl = pictureUrl;
        return this;
    }

    public String getPicturePublicId() {
        return picturePublicId;
    }

    public ProfileViewModel setPicturePublicId(String picturePublicId) {
        this.picturePublicId = picturePublicId;
        return this;
    }
}
