package bg.softuni.fitcom.social;

import org.springframework.security.oauth2.core.oidc.user.DefaultOidcUser;
import org.springframework.security.oauth2.core.oidc.user.OidcUser;

public class FitcomOidcUser extends DefaultOidcUser implements FitcomPrincipal {

    public FitcomOidcUser(OidcUser oidcUser) {
        super(oidcUser.getAuthorities(), oidcUser.getIdToken(), oidcUser.getUserInfo());
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
}
