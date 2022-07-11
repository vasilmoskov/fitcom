package bg.softuni.fitcom.repositories;

import bg.softuni.fitcom.models.entities.PurposeEntity;
import bg.softuni.fitcom.models.enums.PurposeEnum;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface PurposeRepository extends JpaRepository<PurposeEntity, Long> {
    Optional<PurposeEntity> findByName(PurposeEnum name);
}
