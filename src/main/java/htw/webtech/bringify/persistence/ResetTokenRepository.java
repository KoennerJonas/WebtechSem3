package htw.webtech.bringify.persistence;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;
import java.util.Optional;

@Repository
public interface ResetTokenRepository extends JpaRepository<ResetTokenEntity, Long> {

    Optional<ResetTokenEntity> findByToken(String token);
    boolean existsByToken(String token);
    @Transactional
    @Modifying
    @Query("DELETE from reset_token ct where ct.token =?1")
    int deleteResetTokenByToken(String token);

}
