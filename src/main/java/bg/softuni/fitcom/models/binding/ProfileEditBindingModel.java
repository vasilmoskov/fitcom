package bg.softuni.fitcom.models.binding;

import bg.softuni.fitcom.models.enums.RoleEnum;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Positive;
import javax.validation.constraints.Size;
import java.util.List;

public class ProfileEditBindingModel {
    private long id;

    private MultipartFile avatar;

    @NotBlank(message = "Please enter your first name!")
    @Size(min = 1, max = 20, message = "Please enter a valid first name!")
    private String firstName;

    @NotBlank(message = "Please enter your last name!")
    @Size(min = 1, max = 20, message = "Please enter a valid last name!")
    private String lastName;

    @Positive(message = "Age must be a positive number!")
    private Integer age;

    private String email;

//    private List<RoleEnum> roles;

    public long getId() {
        return id;
    }

    public ProfileEditBindingModel setId(long id) {
        this.id = id;
        return this;
    }

    public String getFirstName() {
        return firstName;
    }

    public ProfileEditBindingModel setFirstName(String firstName) {
        this.firstName = firstName;
        return this;
    }

    public String getLastName() {
        return lastName;
    }

    public ProfileEditBindingModel setLastName(String lastName) {
        this.lastName = lastName;
        return this;
    }

    public Integer getAge() {
        return age;
    }

    public ProfileEditBindingModel setAge(Integer age) {
        this.age = age;
        return this;
    }

    public String getEmail() {
        return email;
    }

    public ProfileEditBindingModel setEmail(String email) {
        this.email = email;
        return this;
    }
//
//    public List<RoleEnum> getRoles() {
//        return roles;
//    }
//
//    public ProfileEditBindingModel setRoles(List<RoleEnum> roles) {
//        this.roles = roles;
//        return this;
//    }

    public MultipartFile getAvatar() {
        return avatar;
    }

    public ProfileEditBindingModel setAvatar(MultipartFile avatar) {
        this.avatar = avatar;
        return this;
    }
}
