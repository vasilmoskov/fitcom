package bg.softuni.fitcom.repositories;

import bg.softuni.fitcom.models.entities.BodyPartEntity;
import bg.softuni.fitcom.models.entities.TrainingProgramEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TrainingProgramRepository extends JpaRepository<TrainingProgramEntity, Long> {

    Page<TrainingProgramEntity> findAllByTitleContaining(String title, Pageable pageable);

    Page<TrainingProgramEntity> findAllByBodyPartsIn(List<BodyPartEntity> bodyParts, Pageable pageable);

    Page<TrainingProgramEntity> findAllByTitleContainingAndBodyPartsIn(String title, List<BodyPartEntity> bodyParts, Pageable pageable);

    @Query("select count(t) from TrainingProgramEntity t where t.title like %:title%")
    Integer findCountByContainingTitle(@Param("title") String title);

//    not working :(
//    @Query("select count(t) from TrainingProgramEntity t where :bodyPart in t.bodyParts")
//    Integer findCountByContainingBodyPart(@Param("bodyPart") BodyPartEntity bodyPart);

//    @Query("select count(t) from TrainingProgramEntity t where t.title like %:title% and :bodyPart in t.bodyParts")
//    Integer findCountByContainingTitleAndBodyPart(@Param("title") String title, @Param("bodyPart") BodyPartEntity bodyPart);

    List<TrainingProgramEntity> findAllByBodyPartsIn(List<BodyPartEntity> bodyParts);

    List<TrainingProgramEntity> findAllByTitleContainingAndBodyPartsIn(String title, List<BodyPartEntity> bodyParts);

    List<TrainingProgramEntity> findAllByAuthorEmail(String email);
}
