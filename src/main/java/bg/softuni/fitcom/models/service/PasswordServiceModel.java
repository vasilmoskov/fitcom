package bg.softuni.fitcom.models.service;

import bg.softuni.fitcom.models.validation.PasswordsMatch;

import javax.validation.constraints.NotBlank;

@PasswordsMatch(password = "password", confirmPassword = "confirmPassword")
public class PasswordServiceModel {

    @NotBlank(message = "Please enter a password.")
    private String password;

    @NotBlank(message = "Please enter a confirm password.")
    private String confirmPassword;

    private String email;

    @NotBlank
    private String token;

    public String getPassword() {
        return password;
    }

    public PasswordServiceModel setPassword(String password) {
        this.password = password;
        return this;
    }

    public String getConfirmPassword() {
        return confirmPassword;
    }

    public PasswordServiceModel setConfirmPassword(String confirmPassword) {
        this.confirmPassword = confirmPassword;
        return this;
    }

    public String getEmail() {
        return email;
    }

    public PasswordServiceModel setEmail(String email) {
        this.email = email;
        return this;
    }

    public String getToken() {
        return token;
    }

    public PasswordServiceModel setToken(String token) {
        this.token = token;
        return this;
    }
}
