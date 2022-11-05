package htw.webtech.bringify.persistence;

import htw.webtech.bringify.web.api.Room;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RoomRepository extends JpaRepository<RoomEntity, Long> {

    List<RoomEntity> findAllById(long id);

    Room findById(long id);

}
