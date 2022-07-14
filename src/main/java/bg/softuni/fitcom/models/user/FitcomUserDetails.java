package bg.softuni.fitcom.models.user;

import bg.softuni.fitcom.social.FitcomPrincipal;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;

public class FitcomUserDetails implements UserDetails, FitcomPrincipal {
    private final String password;
    private final String email;
    private final String firstName;
    private final String lastName;
    private final Collection<GrantedAuthority> authorities;

    public FitcomUserDetails(String password, String email, String firstName, String lastName,
                             Collection<GrantedAuthority> authorities) {
        this.password = password;
        this.email = email;
        this.firstName = firstName;
        this.lastName = lastName;
        this.authorities = authorities;
    }

    @Override
    public String getFirstName() {
        return firstName;
    }

    @Override
    public String getLastName() {
        return lastName;
    }

    @Override
    public String getEmail() {
        return email;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return authorities;
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public String getUsername() {
        return email;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }

    public String getGreetingName() {
        if (getFirstName() != null && !getFirstName().isEmpty()) {
            return getFirstName();
        }

        if (getLastName() != null && !getLastName().isEmpty()) {
            return "Mr. " + getLastName();
        }

        return "Anonymous";
    }

    @Override
    public String getName() {
        return email;
    }
}
