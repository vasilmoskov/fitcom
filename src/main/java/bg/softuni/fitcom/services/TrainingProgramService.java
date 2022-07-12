package bg.softuni.fitcom.services;

import bg.softuni.fitcom.models.service.CommentAddServiceModel;
import bg.softuni.fitcom.models.service.TrainingProgramServiceModel;
import bg.softuni.fitcom.models.view.CommentViewModel;
import bg.softuni.fitcom.models.view.TrainingProgramsOverviewViewModel;
import bg.softuni.fitcom.models.view.TrainingProgramDetailsViewModel;
import org.springframework.data.domain.Page;

import java.util.List;

public interface TrainingProgramService {
    List<TrainingProgramsOverviewViewModel> getTrainingPrograms(String title, String bodyPart, Integer pageNo, Integer pageSize, String sortBy);

    TrainingProgramDetailsViewModel getTrainingProgram(long id);

    boolean canModify(String email, long trainingId);

    boolean isInUserFavourites(String email, long trainingId);

    void deleteProgram(long id);

    TrainingProgramServiceModel updateProgram(TrainingProgramServiceModel serviceModel);

    void addToFavourites(long id, String userEmail);

    void removeFromFavourites(long id, String userEmail);

    CommentAddServiceModel addComment(CommentAddServiceModel serviceModel, long id);

    List<CommentViewModel> getComments(long id);

    int getLastPageNumber(String title, String bodyPart, Integer pageSize);

}
