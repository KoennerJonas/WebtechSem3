package htw.webtech.bringify.service;

import htw.webtech.bringify.persistence.RoomEntity;
import htw.webtech.bringify.persistence.RoomRepository;
import htw.webtech.bringify.web.api.Room;
import htw.webtech.bringify.web.api.RoomManipulationRequest;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class RoomService {
    //Datenbank, aus welcher man die Daten bekommt
    private final RoomRepository roomRepository;

    public RoomService(RoomRepository roomRepository) {
        this.roomRepository = roomRepository;
    }

    public List<Room> findAll(){
        List<RoomEntity> rooms = roomRepository.findAll();
        return rooms.stream()
                .map(this::roomEntityToRoom)
                .collect(Collectors.toList());
    }

    public Room findRoomByID(Long id){
        var roomEntity = roomRepository.findById(id);
        return roomEntity.isPresent() ? roomEntityToRoom(roomEntity.get()): null;
    }

    //room wird zurückgesendet, da wir die von der Datenbank erstellte ID haben wollen
    public Room createRoom(RoomManipulationRequest request){
        var roomEntity = new RoomEntity(request.getRoomName(), request.getKeyword(), request.getOwner(), request.getMembers(), request.getItems());
        roomEntity = roomRepository.save(roomEntity);
        return roomEntityToRoom(roomEntity);
    }

    public boolean deleteRoom(Long id){
        if(!roomRepository.existsById(id)){
            return false;
        }else{
            return roomRepository.existsById(id);
        }

    }

    public Room updateRoom(Long id, RoomManipulationRequest request){
        var entityOtionalEmpty = roomRepository.findById(id);
        if (entityOtionalEmpty.isEmpty()){
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
        roomEntity= roomRepository.save(roomEntity);
        return roomEntityToRoom(roomEntity);
    }

    public Room roomEntityToRoom(RoomEntity roomEntity){
        return new Room(roomEntity.getId(),
                roomEntity.getRoomName(),
                roomEntity.getKeyword(),
                roomEntity.getOwner(),
                roomEntity.getMembers(),
                roomEntity.getItems());
    }
}
