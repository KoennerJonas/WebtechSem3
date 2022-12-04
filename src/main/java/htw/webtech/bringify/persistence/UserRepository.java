package htw.webtech.bringify.persistence;

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

    public List<UserEntity> findAllByUsername(String username);
    Optional<UserEntity> findByUsername(String username);
    Boolean existsByUsername(String username);

    boolean existsByMail(String mail);
    Optional<UserEntity> findByMail(String mail);

    @Transactional
    @Modifying
    @Query("UPDATE users u SET u.enabled = true WHERE u.mail = :email")
    void enableUser(@Param("email") String email);

    @Transactional
    @Modifying
    @Query("update users u set u.password = :password where u.mail = :email")
    void setPassword(@Param("email") String email, @Param("password") String password);
}
