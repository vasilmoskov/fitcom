package bg.softuni.fitcom.models.user;

import bg.softuni.fitcom.models.service.UserRegisterServiceModel;
import bg.softuni.fitcom.services.UserService;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.savedrequest.DefaultSavedRequest;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.transaction.Transactional;
import java.io.IOException;
import java.util.ArrayList;

@Configuration("oauth2authSuccessHandler")
public class AuthenticationSuccessHandlerImpl implements AuthenticationSuccessHandler {
    private final UserService userService;

    public AuthenticationSuccessHandlerImpl(UserService userService) {
        this.userService = userService;
    }

    @Override
    @Transactional
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
                                        Authentication authentication) throws IOException {

        if (authentication instanceof OAuth2AuthenticationToken oauthToken) {

            String username = oauthToken.getPrincipal().getName();
            FitcomPrincipal principal = (FitcomPrincipal) oauthToken.getPrincipal();

            // get the last url
            DefaultSavedRequest url = (DefaultSavedRequest) request.getSession()
                    .getAttribute("SPRING_SECURITY_SAVED_REQUEST");

            String redirectUrl = "/";

            if (url != null) {
                redirectUrl = url.getRedirectUrl();
            }

            response.sendRedirect(redirectUrl);

//            UserEntity user = userService.getUser(principal.getEmail());

            if (userService.getUser(principal.getEmail()) != null) {
                return;
            }

            UserRegisterServiceModel registerModel = new UserRegisterServiceModel()
                    .setEmail(principal.getEmail())
                    .setFirstName(principal.getFirstName())
                    .setLastName(principal.getLastName())
                    .setPassword(username)
                    .setRoles(new ArrayList<>())
                    .setPictureUrl("https://res.cloudinary.com/dilbpiicv/image/upload/v1654441524/default_avatar_wmftdp.webp");
            ;

            userService.register(registerModel);
        }
    }
}