package bg.softuni.fitcom.models.service;

import java.util.Date;

public class ResetTokenServiceModel {
    private long id;
    private String token;
    private String email;
    private Date expiryDate;

    public String getToken() {
        return token;
    }

    public ResetTokenServiceModel setToken(String token) {
        this.token = token;
        return this;
    }

    public String getEmail() {
        return email;
    }

    public ResetTokenServiceModel setEmail(String email) {
        this.email = email;
        return this;
    }

    public Date getExpiryDate() {
        return expiryDate;
    }

    public ResetTokenServiceModel setExpiryDate(Date expiryDate) {
        this.expiryDate = expiryDate;
        return this;
    }

    public long getId() {
        return id;
    }

    public ResetTokenServiceModel setId(long id) {
        this.id = id;
        return this;
    }
}
