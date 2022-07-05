package bg.softuni.fitcom.repositories;

import bg.softuni.fitcom.models.entities.RoleEntity;
import bg.softuni.fitcom.models.entities.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<UserEntity, Long> {
    Optional<UserEntity> findByEmail(String email);

    @Modifying
    @Transactional
    @Query("update UserEntity u set u.password = :password where u.email = :email")
    void updatePassword(@Param("password") String password, @Param("email") String email);

    @Query("select u from UserEntity u where u.pendingRoles.size > 0")
    List<UserEntity> findAllByPendingRolesNotEmpty();
}
