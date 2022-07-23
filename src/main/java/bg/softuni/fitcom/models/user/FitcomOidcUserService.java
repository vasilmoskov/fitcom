package bg.softuni.fitcom.models.user;

import bg.softuni.fitcom.repositories.UserRepository;
import org.springframework.security.oauth2.client.oidc.userinfo.OidcUserRequest;
import org.springframework.security.oauth2.client.oidc.userinfo.OidcUserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserService;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.oidc.user.OidcUser;

public class FitcomOidcUserService implements OAuth2UserService<OidcUserRequest, org.springframework.security.oauth2.core.oidc.user.OidcUser> {

    private final UserRepository userRepository;

    private OidcUserService delegate = new OidcUserService();

    public FitcomOidcUserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public OidcUser loadUser(OidcUserRequest userRequest) throws OAuth2AuthenticationException {
        OidcUser user = delegate.loadUser(userRequest);
        return new FitcomOidcUser(user, userRepository);
    }
}
