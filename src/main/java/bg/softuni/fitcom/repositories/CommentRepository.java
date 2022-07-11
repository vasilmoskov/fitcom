package bg.softuni.fitcom.repositories;

import bg.softuni.fitcom.models.entities.CommentEntity;
import bg.softuni.fitcom.models.entities.DietEntity;
import bg.softuni.fitcom.models.entities.TrainingProgramEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CommentRepository extends JpaRepository<CommentEntity, Long> {
    List<CommentEntity> findAllByTrainingProgramAndApproved(TrainingProgramEntity trainingProgram, boolean approved);

    List<CommentEntity> findAllByDietAndApproved(DietEntity diet, boolean approved);

    List<CommentEntity> findAllByApproved(Boolean approved);
}
