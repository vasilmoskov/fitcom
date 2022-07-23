package bg.softuni.fitcom.models.user;

import bg.softuni.fitcom.models.entities.RoleEntity;
import bg.softuni.fitcom.models.entities.UserEntity;
import bg.softuni.fitcom.repositories.UserRepository;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.core.user.OAuth2User;

import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class FacebookUser implements OAuth2User, FitcomPrincipal {
    private String id;
    private String email;
    private String name;

    private final OAuth2User oAuth2User;
    private final UserRepository userRepository;

    public FacebookUser(OAuth2User oAuth2User, UserRepository userRepository) {
        this.oAuth2User = oAuth2User;
        this.userRepository = userRepository;

        this.id = oAuth2User.getAttribute("id");
        this.email = oAuth2User.getAttribute("email");
        this.name = oAuth2User.getAttribute("name");
    }

    @Override
    public Map<String, Object> getAttributes() {
        return Collections.emptyMap();
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        UserEntity userEntity = userRepository
                .findByEmail(oAuth2User.getAttribute("email"))
                .orElse(null);

        if (userEntity == null) {
            return AuthorityUtils.createAuthorityList("ROLE_USER");
        }

        List<RoleEntity> roles = userEntity.getRoles();

        return roles
                .stream()
                .map(r -> new SimpleGrantedAuthority(r.getRole().name()))
                .collect(Collectors.toList());
    }

    @Override
    public String getName() {
        return this.email;
    }

    public String getId() {
        return id;
    }

    public FacebookUser setId(String id) {
        this.id = id;
        return this;
    }

    public String getEmail() {
        return oAuth2User.getAttribute("email");
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
