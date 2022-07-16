package bg.softuni.fitcom.models.user;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.oauth2.core.user.OAuth2User;

import java.util.Collection;
import java.util.Collections;
import java.util.Map;

public class FacebookUser implements OAuth2User, FitcomPrincipal {
    private long id;
    private String email;
    private String name;

    @Override
    public Map<String, Object> getAttributes() {
        return Collections.emptyMap();
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return AuthorityUtils.createAuthorityList("ROLE_USER");
    }

    @Override
    public String getName() {
        return email;
    }

    public long getId() {
        return id;
    }

    public FacebookUser setId(long id) {
        this.id = id;
        return this;
    }

    public String getEmail() {
        return email;
    }

    public FacebookUser setEmail(String email) {
        this.email = email;
        return this;
    }

    public FacebookUser setName(String name) {
        this.name = name;
        return this;
    }

    public String getFirstName() {
        return name.split(" ")[0];
    }

    public String getLastName() {
        return name.split(" ")[1];
    }
}
