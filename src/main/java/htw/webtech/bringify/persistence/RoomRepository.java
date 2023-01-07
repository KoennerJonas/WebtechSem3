package htw.webtech.bringify.persistence;

import htw.webtech.bringify.web.api.Room;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;
import java.util.List;

@Repository
public interface RoomRepository extends JpaRepository<RoomEntity, Long> {

    List<RoomEntity> findAllById(long id);

    Room findById(long id);

    @Transactional
    @Modifying
    @Query("UPDATE rooms r SET r.beschreibung = :description WHERE r.id = :id")
    void saveDescription(@Param("description") String description,@Param("id") Long id);
}
