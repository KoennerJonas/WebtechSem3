package htw.webtech.bringify.persistence;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<UserEntity, Long> {

    public List<UserEntity> findAllByUsername(String username);

    Optional<UserEntity> findByMail(String mail);

    @Transactional
    @Modifying
    @Query("UPDATE users u " +
            "SET u.enabled = TRUE WHERE u.mail = ?1")
    int enableAppUser(String mail);

}
