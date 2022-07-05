package bg.softuni.fitcom.models.service;

import bg.softuni.fitcom.models.enums.RoleEnum;

import java.util.List;

public class UserRegisterServiceModel {
    private String firstName;
    private String lastName;
    private Integer age;
    private String email;
    private String password;
    private String pictureUrl;
    private List<RoleEnum> roles;

    public String getFirstName() {
        return firstName;
    }

    public UserRegisterServiceModel setFirstName(String firstName) {
        this.firstName = firstName;
        return this;
    }

    public String getLastName() {
        return lastName;
    }

    public UserRegisterServiceModel setLastName(String lastName) {
        this.lastName = lastName;
        return this;
    }

    public Integer getAge() {
        return age;
    }

    public UserRegisterServiceModel setAge(Integer age) {
        this.age = age;
        return this;
    }

    public String getEmail() {
        return email;
    }

    public UserRegisterServiceModel setEmail(String email) {
        this.email = email;
        return this;
    }

    public String getPassword() {
        return password;
    }

    public UserRegisterServiceModel setPassword(String password) {
        this.password = password;
        return this;
    }

    public List<RoleEnum> getRoles() {
        return roles;
    }

    public UserRegisterServiceModel setRoles(List<RoleEnum> roles) {
        this.roles = roles;
        return this;
    }

    public String getPictureUrl() {
        return pictureUrl;
    }

    public UserRegisterServiceModel setPictureUrl(String pictureUrl) {
        this.pictureUrl = pictureUrl;
        return this;
    }
}
