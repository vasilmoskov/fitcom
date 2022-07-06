package bg.softuni.fitcom.repositories;

import bg.softuni.fitcom.models.entities.ResetTokenEntity;
import bg.softuni.fitcom.models.entities.VerificationTokenEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ResetTokenRepository extends JpaRepository<ResetTokenEntity, Long> {
    Optional<ResetTokenEntity> findByToken(String token);
}
