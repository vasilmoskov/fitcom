package bg.softuni.fitcom.models.service;

import java.util.Date;

public class TokenServiceModel {
    private long id;
    private String token;
    private String email;
    private Date expiryDate;

    public String getToken() {
        return token;
    }

    public TokenServiceModel setToken(String token) {
        this.token = token;
        return this;
    }

    public String getEmail() {
        return email;
    }

    public TokenServiceModel setEmail(String email) {
        this.email = email;
        return this;
    }

    public Date getExpiryDate() {
        return expiryDate;
    }

    public TokenServiceModel setExpiryDate(Date expiryDate) {
        this.expiryDate = expiryDate;
        return this;
    }

    public long getId() {
        return id;
    }

    public TokenServiceModel setId(long id) {
        this.id = id;
        return this;
    }
}
