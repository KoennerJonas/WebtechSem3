package htw.webtech.bringify.service;

import htw.webtech.bringify.persistence.*;
import htw.webtech.bringify.web.api.Item;
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
    private final ItemRepository itemRepositpry;

    public RoomService(RoomRepository roomRepository, UserRepository userRepository, ItemRepository itemRepositpry) {
        this.roomRepository = roomRepository;
        this.userRepository = userRepository;
        this.itemRepositpry = itemRepositpry;
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
        List<ItemEntity> items = null;
        for (Long i: request.getItems()){
            items.add(itemRepositpry.findById(i).get());
        }

        roomEntity.setRoomName(request.getRoomName());
        roomEntity.setKeyword(request.getKeyword());
        roomEntity.setOwner(request.getOwner());
        roomEntity.setMembers(request.getMembers());
        roomEntity.setItems(items);

        //da mit roomRepository.findById(id) eine Entität mit ID von der Datenbank zurückgegeben wird und diese ID beibehalten wird,
        //weiß das Framework das hier nur aktualisiert werden muss
        roomEntity = roomRepository.save(roomEntity);
        return roomEntityToRoom(roomEntity);
    }

    public void addItemToRoom(Item item ){

        var room = roomRepository.findById(item.getRaumid()).get();
        List<ItemEntity> itemList = room.getItems();

        ItemEntity itemEntity = new ItemEntity(item.getName(),item.getAmmount(),room);
        itemList.add(itemEntity);

        room.setItems(itemList);
        itemRepositpry.save(itemEntity);
        roomRepository.save(room);
    }

    public List<Item> getAllItemsFromRoom(Long raumId){
        var itemEntityList = roomRepository.findById(raumId).get().getItems();
        List<Item> itemList = null;
        for(ItemEntity i : itemEntityList){
            itemList.add(new Item(i.getName(),i.getAmmount(),i.getRoom().getId()));
        }
        return itemList;
    }
    public Room roomEntityToRoom(RoomEntity roomEntity) {

        var itemIds = roomEntity.getItems();
        List<Long> items = null;

        for (ItemEntity i:itemIds){
            items.add(i.getId());
        }

        return new Room(roomEntity.getId(),
                roomEntity.getRoomName(),
                roomEntity.getKeyword(),
                roomEntity.getBeschreibung(),
                roomEntity.getOwner(),
                roomEntity.getMembers(),
                items);
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
