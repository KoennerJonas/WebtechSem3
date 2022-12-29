package htw.webtech.bringify.service;

import htw.webtech.bringify.persistence.*;
import htw.webtech.bringify.web.api.Room;
import htw.webtech.bringify.web.api.RoomManipulationRequest;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class RoomService {
    //Datenbank, aus welcher man die Daten bekommt
    private final RoomRepository roomRepository;
    private final UserRepository userRepository;

    public RoomService(RoomRepository roomRepository, UserRepository userRepository) {
        this.roomRepository = roomRepository;
        this.userRepository = userRepository;
    }

    public List<Room> findAll() {
        List<RoomEntity> rooms = roomRepository.findAll();
        return rooms.stream()
                .map(this::roomEntityToRoom)
                .collect(Collectors.toList());
    }

    public Room findRoomByID(Long id) {
        var roomEntity = roomRepository.findById(id);
        return roomEntity.isPresent() ? roomEntityToRoom(roomEntity.get()) : null;
    }

    //room wird zurückgesendet, da wir die von der Datenbank erstellte ID haben wollen
    public Room createRoom(RoomManipulationRequest request) {
        var roomEntity = new RoomEntity(request.getRoomName(), request.getKeyword(),request.getBeschreibung(), request.getOwner(), request.getMembers(), request.getItems());
        roomEntity = roomRepository.save(roomEntity);
        return roomEntityToRoom(roomEntity);
    }

    public boolean deleteRoom(Long id) {
        if (!roomRepository.existsById(id)) {
            return false;
        }
        roomRepository.deleteById(id);
        return true;
    }

    public Room updateRoom(Long id, RoomManipulationRequest request) {
        var entityOtionalEmpty = roomRepository.findById(id);
        if (entityOtionalEmpty.isEmpty()) {
            return null;
        }

        var roomEntity = entityOtionalEmpty.get();

        roomEntity.setRoomName(request.getRoomName());
        roomEntity.setKeyword(request.getKeyword());
        roomEntity.setOwner(request.getOwner());
        roomEntity.setMembers(request.getMembers());
        roomEntity.setItems(request.getItems());

        //da mit roomRepository.findById(id) eine Entität mit ID von der Datenbank zurückgegeben wird und diese ID beibehalten wird,
        //weiß das Framework das hier nur aktualisiert werden muss
        roomEntity = roomRepository.save(roomEntity);
        return roomEntityToRoom(roomEntity);
    }

    public Room roomEntityToRoom(RoomEntity roomEntity) {
        var itemIds = roomEntity.getItems().stream().map(ItemEntity::getId).collect(Collectors.toList());
        return new Room(roomEntity.getId(),
                roomEntity.getRoomName(),
                roomEntity.getKeyword(),
                roomEntity.getBeschreibung(),
                roomEntity.getOwner(),
                roomEntity.getMembers(),
                itemIds);
    }

    public Boolean addUserToRoom(Long roomid, Long userid) {

        var roomOptional = roomRepository.findById(roomid);
        var userOptional = userRepository.findById(userid);

        if (roomOptional.isEmpty() || userOptional.isEmpty()) {
            return false;
        }
        RoomEntity room = roomOptional.get();
        UserEntity user = userOptional.get();

        Set<UserEntity> userlist = room.getUsers();
        userlist.add(user);
        room.setUsers(userlist);
        roomRepository.save(room);
        return true;
    }
}
