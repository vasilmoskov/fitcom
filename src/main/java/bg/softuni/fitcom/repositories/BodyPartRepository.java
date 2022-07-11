package bg.softuni.fitcom.repositories;

import bg.softuni.fitcom.models.entities.BodyPartEntity;
import bg.softuni.fitcom.models.enums.BodyPartEnum;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface BodyPartRepository extends JpaRepository<BodyPartEntity, Long> {
    Optional<BodyPartEntity> findByName(BodyPartEnum name);
}
