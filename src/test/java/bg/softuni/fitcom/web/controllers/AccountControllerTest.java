package bg.softuni.fitcom.web.controllers;

import bg.softuni.fitcom.DbInitializr;
import bg.softuni.fitcom.util.OnCreateAccountEvent;
import bg.softuni.fitcom.utils.TestControllerUtils;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Primary;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import java.util.UUID;

import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.mock;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.flash;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrl;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

@SpringBootTest
@AutoConfigureMockMvc
public class AccountControllerTest {
    @MockBean
    private DbInitializr dbInitializr;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private TestControllerUtils testDataUtils;

    @Autowired
    private ApplicationEventPublisher eventPublisher;

    @TestConfiguration
    static class MockitoPublisherConfiguration {
        @Bean
        @Primary
        ApplicationEventPublisher publisher() {
            return mock(ApplicationEventPublisher.class);
        }
    }

    @BeforeEach
    void setup() {
        testDataUtils.initRoles();
    }

    @AfterEach()
    void tearDown() {
        testDataUtils.cleanUpDatabase();
    }

    @Test
    void testRegisterPage() throws Exception {
        mockMvc.perform(get("/register"))
                .andExpect(status().isOk())
                .andExpect(model().attributeExists("roles"))
                .andExpect(view().name("register"));
    }

    @Test
    @WithMockUser
    void testGetRegisterPageShouldRedirectToHomePageWhenLoggedInUserTriesToAccessIt() throws Exception {
        mockMvc.perform(get("/register"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/"));
    }

    @Test
    void testDoRegister() throws Exception {
        OnCreateAccountEvent eventMock = mock(OnCreateAccountEvent.class);
        doNothing().when(eventPublisher).publishEvent(eventMock);

        mockMvc.perform(post("/register")
                        .param("firstName", "Vasil")
                        .param("lastName", "Moskov")
                        .param("age", "25")
                        .param("email", "vasko@abv.bg")
                        .param("password", "test")
                        .param("confirmPassword", "test")
                        .with(csrf()))
                .andExpect(status().isOk())
                .andExpect(view().name("email-sent"));
    }

    @Test
    void testDoRegister_WithInvalidData() throws Exception {
        mockMvc.perform(post("/register")
                        .param("firstName", "V8")
                        .param("lastName", "M1")
                        .param("age", "-25")
                        .param("email", "vaskoabv.bg")
                        .param("password", "test")
                        .param("confirmPassword", "nomatch")
                        .with(csrf()))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/register"))
                .andExpect(flash().attributeExists("registerModel"));
    }

    @Test
    void testConfirmAccount() throws Exception {
        String token = UUID.randomUUID().toString();

        testDataUtils.createAccount();
        testDataUtils.createToken(token);

        mockMvc.perform(get("/register/confirm")
                        .queryParam("token", token))
                .andExpect(status().isOk())
                .andExpect(view().name("registration-confirmed"));
    }
}
