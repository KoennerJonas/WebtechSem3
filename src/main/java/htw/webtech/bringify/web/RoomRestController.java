package htw.webtech.bringify.web;

import htw.webtech.bringify.service.RoomService;
import htw.webtech.bringify.service.UserService;
import htw.webtech.bringify.web.api.*;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URISyntaxException;
import java.util.List;

@RestController//sorgt auch dafür, dass dem Browser das Ergebnis der Rest-Methoden als JSON zurückgegeben wird
public class RoomRestController {

    private final RoomService roomService;
    private final UserService userService;

    public RoomRestController(RoomService roomService, UserService userService) {
        this.roomService = roomService;
        this.userService = userService;
    }


    //Get
    @GetMapping(path = "/api/v1/rooms")
    public ResponseEntity<List<Room>> fetchRooms(){
        return ResponseEntity.ok(roomService.findAll());
    }

    //{id} ist der Platzhalter für die Raumvariable
    //der von der Methode entgegen genommene Wert (hier Long id) sollte den gleichen Namen haben, damit Spring das connecten kann
    //PathVariable sagt, dass die Variable aus dem Pfad injeziert werden muss
    @GetMapping(path = "/api/v1/rooms/{id}")
    public ResponseEntity<Room> fetchRoomById(@PathVariable Long id){
    var room = roomService.findRoomByID(id);
    return room != null? ResponseEntity.ok(room): ResponseEntity.notFound().build();
    }


    @PostMapping(path = "/api/v1/create_room")
    public ResponseEntity<String> createRooms(@RequestBody RoomManipulationRequest request) throws URISyntaxException {
        var room =  roomService.createRoom(request);

        var id = Long.toString(room.getId());
        return ResponseEntity.ok(id);
    }

    @PutMapping(path = "/api/v1/rooms/{id}")
    public ResponseEntity<Room> updateRooms(@PathVariable Long id, @RequestBody RoomManipulationRequest request){
        var room = roomService.updateRoom(id, request);
        return room != null ? ResponseEntity.ok(room) : ResponseEntity.notFound().build();
    }

    @PutMapping(path = "/api/v1/rooms/additems")
    public ResponseEntity<Void> addItem(@RequestBody ItemManipulationRequest item){
    roomService.addItemToRoom(item);
    return ResponseEntity.ok().build();
    }

    @GetMapping(path = "/api/v1/rooms/getitems/{id}")
    public ResponseEntity<List<Item>> getItems(@PathVariable Long id){
        var items = roomService.getAllItemsFromRoom(id);
        return items != null? ResponseEntity.ok(items):ResponseEntity.notFound().build();
    }

    @DeleteMapping(path = "/api/v1/rooms/items/{rid}/{iid}")
    public  ResponseEntity<Void> removeItemFromRoom(@PathVariable Long rid, @PathVariable Long iid){
        roomService.deleteItemFromRoom(rid,iid);
        return ResponseEntity.ok().build();
    }
    @DeleteMapping(path = "/api/v1/rooms/user/{rid}/{uid}")
    public ResponseEntity<Void> removeUserFromRoom(@PathVariable Long rid, @PathVariable Long uid){
        roomService.deleteUserFromRoom(rid,uid);
        return ResponseEntity.ok().build();
    }

    @GetMapping(path = "/api/v1/rooms/getuser/{id}")
    public ResponseEntity<List<User>> getUser(@PathVariable Long id){
        var userList = roomService.getAllUserFromRoom(id);
        return userList != null? ResponseEntity.ok(userList):ResponseEntity.notFound().build();
    }

    @DeleteMapping(path = "/api/v1/rooms/{id}")
    public ResponseEntity<Void> deleteRooms(@PathVariable Long id){
        boolean roomFound = roomService.deleteRoom(id);
        return roomFound ? ResponseEntity.ok().build(): ResponseEntity.notFound().build();
    }

    @PutMapping(path = "/api/v1/add_user/{roomid}")
    public ResponseEntity<Void> enroledUser(@PathVariable Long roomid, @RequestBody Username username){
        boolean success = roomService.addUserToRoom(roomid,username);
        return success ? ResponseEntity.ok().build() : ResponseEntity.notFound().build();
    }
    /*
    @PostMapping("/api/v1/user_room")
    public ResponseEntity<List<String>> getRoomNamesFromUser(@RequestBody Long userId){
        var roomList = userService.getAllRoomNamesFromUser(userId);
        return roomList != null? ResponseEntity.ok(roomList):ResponseEntity.notFound().build();
    }*/
    @GetMapping("/api/v1/user_room/{id}")
    public ResponseEntity<List<Room>> getRooms(@PathVariable Long id){
        var roomList = userService.getAllRoomNamesFromUser(id);
        return roomList != null? ResponseEntity.ok(roomList):ResponseEntity.notFound().build();
    }

    @PutMapping("/api/v1/description/{id}")
    public ResponseEntity<Void> saveDescription(@PathVariable Long id, @RequestBody Description description){
        roomService.saveDescription(id,description);
        return ResponseEntity.ok().build();
    }
    @GetMapping("/api/v1/description/{id}")
    public ResponseEntity<Description> getDescription (@PathVariable Long id){
        return roomService.getDescription(id) != null? ResponseEntity.ok(roomService.getDescription(id)):ResponseEntity.notFound().build();
    }

    @GetMapping("/api/v1/username_item/{itemId}/{userId}")
    public ResponseEntity<Username> setUsernameToItem(@PathVariable Long itemId, @PathVariable Long userId){
       Username username= userService.setUsernameToItem(itemId,userId);
        return username != null? ResponseEntity.ok(username):ResponseEntity.notFound().build();
    }
    @GetMapping("/api/v1/room_name/{roomId}")
    public ResponseEntity<RoomName> getRoomName(@PathVariable Long roomId){
        return ResponseEntity.ok(roomService.getRoomName(roomId));
    }

}
