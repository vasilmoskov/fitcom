package bg.softuni.fitcom.models.user;

import bg.softuni.fitcom.exceptions.ResourceNotFoundException;
import bg.softuni.fitcom.models.entities.RoleEntity;
import bg.softuni.fitcom.models.entities.UserEntity;
import bg.softuni.fitcom.repositories.UserRepository;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.core.oidc.user.DefaultOidcUser;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

public class FitcomOidcUser extends DefaultOidcUser implements FitcomPrincipal {

    private final UserRepository userRepository;

    public FitcomOidcUser(org.springframework.security.oauth2.core.oidc.user.OidcUser oidcUser, UserRepository userRepository) {
        super(oidcUser.getAuthorities(), oidcUser.getIdToken(), oidcUser.getUserInfo());
        this.userRepository = userRepository;
    }

    @Override
    public String getFirstName() {
        return super.getGivenName();
    }

    @Override
    public String getLastName() {
        return super.getFamilyName();
    }

    @Override
    public String getEmail() {
        return super.getEmail();
    }

    @Override
    public String getName() {
        return super.getEmail();
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {

        UserEntity userEntity = userRepository
                .findByEmail(getEmail())
                .orElseThrow(() -> new ResourceNotFoundException("No such user!"));

        List<RoleEntity> roles = userEntity.getRoles();

        return  roles
                .stream()
                .map(r -> new SimpleGrantedAuthority(r.getRole().name()))
                .collect(Collectors.toList());
    }
}
