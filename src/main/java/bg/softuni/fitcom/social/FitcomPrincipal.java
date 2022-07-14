package bg.softuni.fitcom.social;

import org.springframework.security.core.AuthenticatedPrincipal;

public interface FitcomPrincipal extends AuthenticatedPrincipal {
    String getFirstName();

    String getLastName();

    String getEmail();

    default String getGreetingName() {
        if (getFirstName() != null && !getFirstName().isEmpty()) {
            return getFirstName();
        }

        if (getLastName() != null && !getLastName().isEmpty()) {
            return "Mr. " + getLastName();
        }

        return "Anonymous";
    }
}
