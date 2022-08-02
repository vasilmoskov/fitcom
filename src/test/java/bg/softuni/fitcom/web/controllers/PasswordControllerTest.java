package bg.softuni.fitcom.web.controllers;

import bg.softuni.fitcom.models.entities.TokenEntity;
import bg.softuni.fitcom.repositories.TokenRepository;
import bg.softuni.fitcom.util.OnPasswordResetEvent;
import bg.softuni.fitcom.utils.TestDataUtils;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Primary;
import org.springframework.test.web.servlet.MockMvc;

import java.util.UUID;

import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.mock;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.flash;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrl;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

@SpringBootTest
@AutoConfigureMockMvc
public class PasswordControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private TestDataUtils testDataUtils;

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

    @AfterEach
    void tearDown() {
        testDataUtils.cleanUpDatabase();
    }

    @Test
    void testGetPasswordReset() throws Exception {
        mockMvc.perform(get("/password"))
                .andExpect(status().isOk())
                .andExpect(view().name("password"));
    }

    @Test
    void testDoResetPassword() throws Exception {
        OnPasswordResetEvent eventMock = mock(OnPasswordResetEvent.class);
        doNothing().when(eventPublisher).publishEvent(eventMock);

        mockMvc.perform(post("/password")
                        .param("email", "vasko@abv.bg")
                        .with(csrf()))
                .andExpect(status().isOk())
                .andExpect(view().name("email-sent"));
    }

    @Test
    void testGetNewPassword() throws Exception {
        String token = UUID.randomUUID().toString();

        mockMvc.perform(get("/password-reset")
                        .queryParam("token", token))
                .andExpect(status().isOk())
                .andExpect(view().name("reset-password"));
    }

    @Test
    void testSaveNewPassword() throws Exception {
        String token = UUID.randomUUID().toString();
        testDataUtils.createToken(token);

        mockMvc.perform(post("/password-reset")
                        .param("password", "new")
                        .param("confirmPassword", "new")
                        .queryParam("token", token)
                        .with(csrf()))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/login"));
    }

    @Test
    void testSaveNewPassword_PasswordsDontMatch() throws Exception {
        String token = UUID.randomUUID().toString();
        testDataUtils.createToken(token);

        mockMvc.perform(post("/password-reset")
                        .param("password", "new")
                        .param("confirmPassword", "neww")
                        .queryParam("token", token)
                        .with(csrf()))
                .andExpect(status().is3xxRedirection())
                .andExpect(flash().attributeExists("passwordModel"));
    }
}
