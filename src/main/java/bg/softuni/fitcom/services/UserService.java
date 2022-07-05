package bg.softuni.fitcom.services;

import bg.softuni.fitcom.models.entities.UserEntity;
import bg.softuni.fitcom.models.enums.RoleEnum;
import bg.softuni.fitcom.models.service.ProfileEditServiceModel;
import bg.softuni.fitcom.models.service.UserRegisterServiceModel;
import bg.softuni.fitcom.models.view.ApplicationsViewModel;
import bg.softuni.fitcom.models.view.CommentViewModel;
import bg.softuni.fitcom.models.view.DietOverviewViewModel;
import bg.softuni.fitcom.models.view.ProfileViewModel;
import bg.softuni.fitcom.models.view.TrainingProgramsOverviewViewModel;

import javax.transaction.Transactional;
import java.util.List;

public interface UserService {
    String getFirstName(String email);

    UserEntity getUser(String email);

    UserRegisterServiceModel register(UserRegisterServiceModel serviceModel);

    ProfileViewModel getProfile(String email);

    List<TrainingProgramsOverviewViewModel> getFavouriteTrainingPrograms(String email);

    ProfileEditServiceModel editProfile(ProfileEditServiceModel serviceModel);

    List<TrainingProgramsOverviewViewModel> getPostedTrainingPrograms(String email);

    List<DietOverviewViewModel> getFavouriteDiets(String email);

    List<DietOverviewViewModel> getPostedDiets(String email);

    boolean isAdmin(String email);

    boolean hasApplied(String email, RoleEnum role);

    void applyForNutritionist(String email);

    void applyForTrainer(String email);

    List<ApplicationsViewModel> getApplications();

    void approveApplication(long userId);

    void deleteApplication(long userId);

}
