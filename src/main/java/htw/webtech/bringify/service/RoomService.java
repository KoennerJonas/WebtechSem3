package htw.webtech.bringify.service;

import htw.webtech.bringify.persistence.*;
import htw.webtech.bringify.web.api.Item;
import htw.webtech.bringify.web.api.Room;
import htw.webtech.bringify.web.api.RoomManipulationRequest;
import htw.webtech.bringify.web.api.User;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class RoomService {
    //Datenbank, aus welcher man die Daten bekommt
    private final RoomRepository roomRepository;
    private final UserRepository userRepository;
    private final ItemRepository itemRepository;

    public RoomService(RoomRepository roomRepository, UserRepository userRepository, ItemRepository itemRepositpry) {
        this.roomRepository = roomRepository;
        this.userRepository = userRepository;
        this.itemRepository = itemRepositpry;
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
        var roomEntity = new RoomEntity(request.getRoomName(), request.getKeyword(),request.getBeschreibung(), request.getOwner(), request.getItems());
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
        List<ItemEntity> items = new ArrayList();
        for (Long i: request.getItems()){
            items.add(itemRepository.findById(i).get());
        }

        roomEntity.setRoomName(request.getRoomName());
        roomEntity.setKeyword(request.getKeyword());
        roomEntity.setOwner(request.getOwner());
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
        itemRepository.save(itemEntity);
        roomRepository.save(room);
        System.out.println(room.getItems().get(0).getName());
    }

    public List<Item> getAllItemsFromRoom(Long raumId){

        var itemEntityList = roomRepository.findById(raumId).get().getItems();
        List<Item> itemList = new ArrayList<>();
        for(ItemEntity i : itemEntityList){
            itemList.add(new Item(i.getName(),i.getAmmount(),i.getRoom().getId()));
        }
        return itemList;
    }

    public void deleteUserFromRoom(Long roomId, Long userId){
        var roomEntity = roomRepository.findById(roomId).get();
        var userEntity = userRepository.findById(userId).get();
        var userSet = roomEntity.getUsers();

        userSet.remove(userEntity);

        roomEntity.setUsers(userSet);
        roomRepository.save(roomEntity);
    }

    public void deleteItemFromRoom(Long roomId, Long itemId){
        var roomEntity = roomRepository.findById(roomId).get();
        var roomItemList = roomEntity.getItems();
        var roomitem = itemRepository.findById(itemId).get();

        //itemRepository.deleteById(roomitem.getId());
        roomItemList.remove(roomitem);

        roomEntity.setItems(roomItemList);
        roomRepository.save(roomEntity);
    }

    public List<User> getAllUserFromRoom(Long raumId){
        var userEntityList = roomRepository.findById(raumId).get().getUsers();
        List<User> userList = new ArrayList<>();

        for(UserEntity u: userEntityList){
            userList.add(new User(u.getId(),u.getUsername(), u.getMail(), u.getPassword()));
        }
        return userList;
    }
    public Room roomEntityToRoom(RoomEntity roomEntity) {
        List<Long> items = new ArrayList<>();
        if(roomEntity.getItems() != null){
            var itemIds = roomEntity.getItems();

            for (ItemEntity i:itemIds){
                items.add(i.getId());
            }
        }


        return new Room(roomEntity.getId(),
                roomEntity.getRoomName(),
                roomEntity.getKeyword(),
                roomEntity.getBeschreibung(),
                roomEntity.getOwner(),
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
