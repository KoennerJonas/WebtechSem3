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
public interface ItemRepository extends JpaRepository<ItemEntity, Long> {

    Optional<ItemEntity> findById(Long i);
    @Transactional
    @Modifying
    @Query("UPDATE Item i SET i.username= :username WHERE i.id = :id")
    void addUsername(@Param("username") String username, @Param("id") Long id);

}
