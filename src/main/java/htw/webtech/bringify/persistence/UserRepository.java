package htw.webtech.bringify.persistence;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<UserEntity, Long> {

    public List<UserEntity> findAllByUsername(String username);

    Optional<UserEntity> findByEmail(String email);

}
