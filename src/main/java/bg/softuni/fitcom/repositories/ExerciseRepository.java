package bg.softuni.fitcom.repositories;

import bg.softuni.fitcom.models.entities.ExerciseEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ExerciseRepository extends JpaRepository<ExerciseEntity, Long> {
    Optional<ExerciseEntity> findByName(String name);
}
