package bg.softuni.fitcom.web.controllers;

import bg.softuni.fitcom.models.entities.RoleEntity;
import bg.softuni.fitcom.models.enums.RoleEnum;
import bg.softuni.fitcom.repositories.AccountRepository;
import bg.softuni.fitcom.repositories.RoleRepository;
import bg.softuni.fitcom.util.OnCreateAccountEvent;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Primary;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.mock;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.flash;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

@SpringBootTest
@AutoConfigureMockMvc
public class AccountControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private AccountRepository accountRepository;

    @Autowired
    private ApplicationEventPublisher eventPublisher;

    @BeforeEach
    void setup() {
        RoleEntity user = new RoleEntity().setRole(RoleEnum.USER);
        roleRepository.save(user);
    }

    @AfterEach()
    void tearDown() {
        accountRepository.deleteAll();
        roleRepository.deleteAll();
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
                .andExpect(view().name("redirect:/"));
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
                .andExpect(view().name("redirect:/register"))
                .andExpect(flash().attributeExists("registerModel"));
    }

    @TestConfiguration
    static class MockitoPublisherConfiguration {
        @Bean
        @Primary
        ApplicationEventPublisher publisher() {
            return mock(ApplicationEventPublisher.class);
        }
    }
}
