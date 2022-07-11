package bg.softuni.fitcom.repositories;

import bg.softuni.fitcom.models.entities.DietEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface DietRepository extends JpaRepository<DietEntity, Long> {
    List<DietEntity> findAllByAuthorEmail(String email);
}
