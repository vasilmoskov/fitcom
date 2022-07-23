package bg.softuni.fitcom.repositories;

import bg.softuni.fitcom.models.entities.GoalEntity;
import bg.softuni.fitcom.models.enums.GoalEnum;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface GoalRepository extends JpaRepository<GoalEntity, Long> {
    Optional<GoalEntity> findByName(GoalEnum name);
}
