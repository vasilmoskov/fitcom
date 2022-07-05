package bg.softuni.fitcom.models.service;

import bg.softuni.fitcom.models.enums.RoleEnum;

import java.util.List;

public class AccountRegisterServiceModel {
    private String firstName;
    private String lastName;
    private Integer age;
    private String email;
    private String password;
    private String pictureUrl;
    private String picturePublicId;
    private List<RoleEnum> roles;

    public String getFirstName() {
        return firstName;
    }

    public AccountRegisterServiceModel setFirstName(String firstName) {
        this.firstName = firstName;
        return this;
    }

    public String getLastName() {
        return lastName;
    }

    public AccountRegisterServiceModel setLastName(String lastName) {
        this.lastName = lastName;
        return this;
    }

    public Integer getAge() {
        return age;
    }

    public AccountRegisterServiceModel setAge(Integer age) {
        this.age = age;
        return this;
    }

    public String getEmail() {
        return email;
    }

    public AccountRegisterServiceModel setEmail(String email) {
        this.email = email;
        return this;
    }

    public String getPassword() {
        return password;
    }

    public AccountRegisterServiceModel setPassword(String password) {
        this.password = password;
        return this;
    }

    public List<RoleEnum> getRoles() {
        return roles;
    }

    public AccountRegisterServiceModel setRoles(List<RoleEnum> roles) {
        this.roles = roles;
        return this;
    }

    public String getPictureUrl() {
        return pictureUrl;
    }

    public AccountRegisterServiceModel setPictureUrl(String pictureUrl) {
        this.pictureUrl = pictureUrl;
        return this;
    }

    public String getPicturePublicId() {
        return picturePublicId;
    }

    public AccountRegisterServiceModel setPicturePublicId(String picturePublicId) {
        this.picturePublicId = picturePublicId;
        return this;
    }
}
