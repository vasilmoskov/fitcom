package bg.softuni.fitcom.models.entities;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import java.util.Calendar;
import java.util.Date;

@Entity
@Table(name = "reset_tokens")
public class ResetTokenEntity extends BaseEntity {
    public static final int EXPIRATION = 60 * 24;

    @Column
    private String token;

    @Column
    private String email;

    @Column
    private Date expiryDate;

    public String getToken() {
        return token;
    }

    public ResetTokenEntity setToken(String token) {
        this.token = token;
        return this;
    }

    public String getEmail() {
        return email;
    }

    public ResetTokenEntity setEmail(String username) {
        this.email = username;
        return this;
    }

    public Date getExpiryDate() {
        return expiryDate;
    }

    public ResetTokenEntity setExpiryDate() {
        this.expiryDate = calculateExpiryDate(EXPIRATION);
        return this;
    }

    public Date calculateExpiryDate(int expiryTimeInMinutes) {
        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.MINUTE, expiryTimeInMinutes);
        return cal.getTime();
    }
}
