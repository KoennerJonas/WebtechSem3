package htw.webtech.bringify.service;

import htw.webtech.bringify.persistence.*;
import htw.webtech.bringify.web.api.*;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class RoomService {
    //Datenbank, aus welcher man die Daten bekommt
    private final RoomRepository roomRepository;
    private final UserRepository userRepository;
    private final ItemRepository itemRepository;



    public RoomService(RoomRepository roomRepository, UserRepository userRepository, ItemRepository itemRepository) {
        this.roomRepository = roomRepository;
        this.userRepository = userRepository;
        this.itemRepository = itemRepository;

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
        Set<UserEntity> users = new HashSet<>();
        users.add(userRepository.findById(request.getOwner()).get());
        var roomEntity = new RoomEntity(request.getRoomName(), request.getKeyword(),request.getBeschreibung(), request.getOwner(), request.getItems(),users);

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
        Set<ItemEntity> items = new HashSet<>();

        if(request.getItems() != null){
            for (ItemEntity i: request.getItems()){
                items.add(i);
            }
        }


        roomEntity.setRoomName(request.getRoomName());
        roomEntity.setKeyword(request.getKeyword());
        roomEntity.setBeschreibung(request.getBeschreibung());
        roomEntity.setOwner(request.getOwner());
        roomEntity.setItems(items);

        //da mit roomRepository.findById(id) eine Entität mit ID von der Datenbank zurückgegeben wird und diese ID beibehalten wird,
        //weiß das Framework das hier nur aktualisiert werden muss
        roomEntity = roomRepository.save(roomEntity);
        return roomEntityToRoom(roomEntity);
    }

    public void addItemToRoom(ItemManipulationRequest item ){

        var room = roomRepository.findById(item.getRaumid()).get();
        Set<ItemEntity> itemList = room.getItems();

        ItemEntity itemEntity = new ItemEntity(item.getName(),item.getAmmount(),room,null);
        itemList.add(itemEntity);

        room.setItems(itemList);
        itemRepository.save(itemEntity);
        roomRepository.save(room);
    }

    public Set<Item> getAllItemsFromRoom(Long raumId){

        var roomEntity = roomRepository.findById(raumId);
        var itemEntityList = roomEntity.get().getItems();
        Set<Item> itemList = new HashSet<>();
        for(ItemEntity i : itemEntityList){
            itemList.add(new Item(i.getId(),i.getName(),i.getAmmount(),i.getRoom().getId(),i.getUsername()));

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
        var userEntity = roomRepository.findById(raumId);
        var userEntitySet = userEntity.get().getUsers();
        List<User> userList = new ArrayList<>();

        for(UserEntity u: userEntitySet){
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

        Room room = new Room(roomEntity.getId(),
                roomEntity.getRoomName(),
                roomEntity.getKeyword(),
                roomEntity.getBeschreibung(),
                roomEntity.getOwner(),
                items);
        if(roomEntity.getUsers() ==null || roomEntity.getUsers().isEmpty() ){
            return room;
        }else{
            room.setUsers(roomEntity.getUsers());
            return room;
        }
    }

    public Boolean addUserToRoom(Long roomid, Username username) {

        var roomOptional = roomRepository.findById(roomid);
        var userOptional = userRepository.findByUsername(username.getUsername());

        if (roomOptional.isEmpty() || userOptional.isEmpty()) {
            return false;
        }
        RoomEntity room = roomOptional.get();
        UserEntity user = userOptional.get();

        Set<UserEntity> userlist = room.getUsers();
        userlist.add(user);
        room.setUsers(userlist);
        var test = roomRepository.save(room);
        return true;
    }
    public void saveDescription(Long id, Description description){
        roomRepository.saveDescription(description.getDescription(),id);
    }
    public Description getDescription(Long id){
        var roomEntityOptional = roomRepository.findById(id);
        var roomEntity = roomEntityOptional.get();
        Room room = roomEntityToRoom(roomEntity);
        if(room.getBeschreibung() ==null){
            return new Description("");
        }
        return new Description(room.getBeschreibung());
    }
    public RoomName getRoomName(Long id){
        var roomEntity = roomRepository.findById(id).get();
        Room room = roomEntityToRoom(roomEntity);
        return new RoomName( room.getRoomName());
    }

}
