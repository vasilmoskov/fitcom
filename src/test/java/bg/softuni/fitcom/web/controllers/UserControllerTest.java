package bg.softuni.fitcom.web.controllers;

import bg.softuni.fitcom.models.entities.RoleEntity;
import bg.softuni.fitcom.models.entities.UserEntity;
import bg.softuni.fitcom.models.enums.RoleEnum;
import bg.softuni.fitcom.models.user.FitcomUser;
import bg.softuni.fitcom.repositories.RoleRepository;
import bg.softuni.fitcom.repositories.UserRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.flash;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrl;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

@SpringBootTest
@AutoConfigureMockMvc
public class UserControllerTest {

    private final FitcomUser user = new FitcomUser(
            "test",
            "georgi@abv.bg",
            "Georgi",
            "Georgiev",
            List.of(new SimpleGrantedAuthority("USER"))
    );

    private final FitcomUser admin = new FitcomUser(
            "test",
            "admin@abv.bg",
            "Admin",
            "Admin",
            List.of(new SimpleGrantedAuthority("ADMIN"))
    );

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @BeforeEach
    void init() {
        insertRoles();
        createUser();
    }

    @AfterEach
    void tearDown() {
        userRepository.deleteAll();
        roleRepository.deleteAll();
    }

    @Test
    void testGetProfile() throws Exception {
        mockMvc.perform(get("/profile").with(user(user)))
                .andExpect(status().isOk())
                .andExpect(view().name("profile"));
    }

    @Test
    void testGetEditProfile() throws Exception {
        mockMvc.perform(get("/profile/edit").with(user(user)))
                .andExpect(status().isOk())
                .andExpect(view().name("profile-edit"));
    }

    @Test
    void testEditProfile() throws Exception {
        mockMvc.perform(put("/profile/edit")
                        .with(user(user))
                        .with(csrf())
                        .param("firstName", "Gosho")
                        .param("lastName", "Geshev")
                        .param("email", "gosho@abv.bg")
                        .param("age", "44"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/profile"));
    }

    @Test
    void testEditProfileWithInvalidData() throws Exception {
        mockMvc.perform(put("/profile/edit")
                        .with(user(user))
                        .with(csrf())
                        .param("firstName", "G8")
                        .param("lastName", "G7")
                        .param("email", "goshoabv.bg")
                        .param("age", "-44"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("edit-errors"))
                .andExpect(flash().attributeExists("userModel"));
    }

    @Test
    void testGetFavourites() throws Exception {
        mockMvc.perform(get("/favourites").with(user(user)))
                .andExpect(status().isOk())
                .andExpect(view().name("favourites"))
                .andExpect(model().attributeExists("diets", "trainingPrograms"));
    }

    @Test
    void testGetPosts() throws Exception {
        mockMvc.perform(get("/posts").with(user(user)))
                .andExpect(status().isOk())
                .andExpect(view().name("posts"))
                .andExpect(model().attributeExists("diets", "trainingPrograms"));
    }

    @Test
    void testApplyForNutritionist() throws Exception {
        mockMvc.perform(post("/apply/nutritionist")
                        .with(user(user))
                        .with(csrf()))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/"));
    }

    @Test
    void testApplyForTrainer() throws Exception {
        mockMvc.perform(post("/apply/trainer")
                        .with(user(user))
                        .with(csrf()))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/"));
    }

    @Test
    void testGetPendingApplications_returnsForbiddenWhenUserIsNotAdmin() throws Exception {
        mockMvc.perform(get("/pending/applications")
                        .with(user(user)))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/forbidden"));
    }

    @Test
    void testGetPendingApplications() throws Exception {
        createAdmin();

        mockMvc.perform(get("/pending/applications")
                        .with(user(admin)))
                .andExpect(status().isOk())
                .andExpect(view().name("applications"))
                .andExpect(model().attributeExists("applications"));
    }

    @Test
    void testApprove() throws Exception {
        createAdmin();
        UserEntity applicant = getApplicant();

        mockMvc.perform(post("/pending/applications/" + applicant.getId())
                        .with(csrf())
                        .with(user(admin)))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/pending/applications"));
    }

    @Test
    void testApprove_returnsForbiddenWhenAccessedByUser() throws Exception {
        UserEntity applicant = getApplicant();

        mockMvc.perform(post("/pending/applications/" + applicant.getId())
                        .with(csrf())
                        .with(user(user)))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/forbidden"));
    }

    @Test
    void testDelete() throws Exception {
        createAdmin();
        UserEntity applicant = getApplicant();

        mockMvc.perform(delete("/pending/applications/" + applicant.getId())
                        .with(csrf())
                        .with(user(admin)))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/pending/applications"));
    }

    @Test
    void testDelete_returnsForbiddenWhenAccessedByUser() throws Exception {
        UserEntity applicant = getApplicant();

        mockMvc.perform(delete("/pending/applications/" + applicant.getId())
                        .with(csrf())
                        .with(user(user)))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/forbidden"));
    }

    @Test
    void testGetStats() throws Exception {
        createAdmin();

        mockMvc.perform(get("/stats")
                .with(user(admin)))
                .andExpect(status().isOk())
                .andExpect(view().name("stats"))
                .andExpect(model().attributeExists("trainingProgramsToViews", "dietsToViews"));
    }

    @Test
    void testGetStats_returnsForbiddenWhenAccessedByUser() throws Exception {
        mockMvc.perform(get("/stats")
                        .with(user(user)))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/forbidden"));
    }

    private UserEntity getApplicant() {
        UserEntity userEntity = userRepository.findByEmail("georgi@abv.bg").get();
        userEntity.setPendingRoles(List.of(roleRepository.findByRole(RoleEnum.ADMIN).get()));
        return userRepository.save(userEntity);
    }

    private void insertRoles() {
        RoleEntity user = new RoleEntity()
                .setRole(RoleEnum.USER);

        RoleEntity trainer = new RoleEntity()
                .setRole(RoleEnum.TRAINER);

        RoleEntity nutritionist = new RoleEntity()
                .setRole(RoleEnum.NUTRITIONIST);

        RoleEntity admin = new RoleEntity()
                .setRole(RoleEnum.ADMIN);

        roleRepository.saveAll(List.of(user, trainer, nutritionist, admin));
    }

    private void createUser() {
        UserEntity user = new UserEntity()
                .setFirstName("Georgi")
                .setLastName("Georgiev")
                .setEmail("georgi@abv.bg")
                .setPassword(passwordEncoder.encode("test"));

        userRepository.save(user);
    }

    private void createAdmin() {
        UserEntity user = new UserEntity()
                .setFirstName("Admin")
                .setLastName("Admin")
                .setEmail("admin@abv.bg")
                .setPassword(passwordEncoder.encode("test"))
                .setRoles(List.of(roleRepository.findByRole(RoleEnum.ADMIN).get()));

        userRepository.save(user);
    }
}
