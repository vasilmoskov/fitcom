package bg.softuni.fitcom.config;

import bg.softuni.fitcom.social.AuthenticationSuccessHandlerImpl;
import bg.softuni.fitcom.social.FacebookUser;
import bg.softuni.fitcom.social.FitcomOidcUserService;
import org.springframework.boot.autoconfigure.security.servlet.PathRequest;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
public class SecurityConfig extends WebSecurityConfigurerAdapter {
    private final UserDetailsService userDetailsService;
    private final PasswordEncoder passwordEncoder;
    private final AccessDeniedHandler accessDeniedHandler;
    private final AuthenticationSuccessHandlerImpl authenticationSuccessHandler;

    public SecurityConfig(UserDetailsService userDetailsService, PasswordEncoder passwordEncoder,
                          AccessDeniedHandler accessDeniedHandler,
                          AuthenticationSuccessHandlerImpl authenticationSuccessHandler) {
        this.userDetailsService = userDetailsService;
        this.passwordEncoder = passwordEncoder;
        this.accessDeniedHandler = accessDeniedHandler;
        this.authenticationSuccessHandler = authenticationSuccessHandler;
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .authorizeRequests()
                .requestMatchers(PathRequest.toStaticResources().atCommonLocations()).permitAll()
                .antMatchers("/", "/register/**", "/login",
                        "/training-programs", "/diets", "/password*").permitAll()
                .antMatchers("/training-programs/add").hasAnyAuthority("TRAINER", "ADMIN")
                .antMatchers("/diets/add").hasAnyAuthority("NUTRITIONIST", "ADMIN")
                .antMatchers("/pending/**", "/stats").hasAuthority("ADMIN")
                .antMatchers("/**").authenticated()
            .and()
                .exceptionHandling()
                .accessDeniedHandler(accessDeniedHandler)
            .and()
                .formLogin()
                .loginPage("/login")
                .usernameParameter("email")
                .passwordParameter(UsernamePasswordAuthenticationFilter.SPRING_SECURITY_FORM_PASSWORD_KEY)
                .defaultSuccessUrl("/")
                .failureForwardUrl("/login-error")
            .and()
                .oauth2Login()
                .loginPage("/login")
                .successHandler(this.authenticationSuccessHandler)
                .userInfoEndpoint().customUserType(FacebookUser.class, "facebook")
                .oidcUserService(new FitcomOidcUserService())
                .and()
            .and()
                .logout()
                .logoutUrl("/logout")
                .logoutSuccessUrl("/")
                .invalidateHttpSession(true)
                .deleteCookies("JSESSIONID");
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(userDetailsService)
                .passwordEncoder(passwordEncoder);
    }
}
