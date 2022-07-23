package bg.softuni.fitcom.models.user;

import bg.softuni.fitcom.repositories.UserRepository;
import org.springframework.security.oauth2.client.oidc.userinfo.OidcUserRequest;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserService;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;

public class OidcUserService implements OAuth2UserService<OidcUserRequest, org.springframework.security.oauth2.core.oidc.user.OidcUser> {

    private final UserRepository userRepository;

    private org.springframework.security.oauth2.client.oidc.userinfo.OidcUserService delegate = new org.springframework.security.oauth2.client.oidc.userinfo.OidcUserService();

    public OidcUserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public org.springframework.security.oauth2.core.oidc.user.OidcUser loadUser(OidcUserRequest userRequest) throws OAuth2AuthenticationException {
        org.springframework.security.oauth2.core.oidc.user.OidcUser user = delegate.loadUser(userRequest);
        return new OidcUser(user, userRepository);
    }
}
