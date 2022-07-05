package bg.softuni.fitcom.models.binding;

import bg.softuni.fitcom.models.enums.RoleEnum;
import bg.softuni.fitcom.models.validation.NotOccupied;
import bg.softuni.fitcom.models.validation.PasswordsMatch;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.constraints.Email;
import javax.validation.constraints.Max;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Positive;
import javax.validation.constraints.Size;
import java.util.List;

@PasswordsMatch(password = "password", confirmPassword = "confirmPassword")
public class AccountRegisterBindingModel {
    private MultipartFile avatar;

    @NotBlank(message = "Please enter your first name!")
    @Size(min = 1, max = 20, message = "First name should be between 1 and 20 characters!")
    @Pattern(regexp = "[a-zA-Z]+", message = "First name should consist only of letters.")
    private String firstName;

    @NotBlank(message = "Please enter your last name!")
    @Size(min = 1, max = 20, message = "Last name should be between 1 and 20 characters!")
    @Pattern(regexp = "[a-zA-Z]+", message = "Last name should consist only of letters.")
    private String lastName;

    @Positive(message = "Age must be a positive number!")
    @Max(value = 100, message = "Age cannot be above 100.")
    private Integer age;

    @NotBlank(message = "Please enter an email address!")
    @NotOccupied(message = "Email is already occupied!")
    @Email(message = "Please enter a valid email address!")
    private String email;

    @NotBlank(message = "Please enter a password!")
    private String password;

    @NotBlank(message = "Please enter a confirm password!")
    private String confirmPassword;

    private List<RoleEnum> roles;

    public String getFirstName() {
        return firstName;
    }

    public AccountRegisterBindingModel setFirstName(String firstName) {
        this.firstName = firstName;
        return this;
    }

    public String getLastName() {
        return lastName;
    }

    public AccountRegisterBindingModel setLastName(String lastName) {
        this.lastName = lastName;
        return this;
    }

    public Integer getAge() {
        return age;
    }

    public AccountRegisterBindingModel setAge(Integer age) {
        this.age = age;
        return this;
    }

    public String getEmail() {
        return email;
    }

    public AccountRegisterBindingModel setEmail(String email) {
        this.email = email;
        return this;
    }

    public String getPassword() {
        return password;
    }

    public AccountRegisterBindingModel setPassword(String password) {
        this.password = password;
        return this;
    }

    public List<RoleEnum> getRoles() {
        return roles;
    }

    public AccountRegisterBindingModel setRoles(List<RoleEnum> roles) {
        this.roles = roles;
        return this;
    }

    public String getConfirmPassword() {
        return confirmPassword;
    }

    public AccountRegisterBindingModel setConfirmPassword(String confirmPassword) {
        this.confirmPassword = confirmPassword;
        return this;
    }

    public MultipartFile getAvatar() {
        return avatar;
    }

    public AccountRegisterBindingModel setAvatar(MultipartFile avatar) {
        this.avatar = avatar;
        return this;
    }
}
