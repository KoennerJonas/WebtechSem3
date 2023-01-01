package htw.webtech.bringify.web;

import htw.webtech.bringify.service.RoomService;
import htw.webtech.bringify.web.api.Item;
import htw.webtech.bringify.web.api.Room;
import htw.webtech.bringify.web.api.RoomManipulationRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;

@RestController//sorgt auch dafür, dass dem Browser das Ergebnis der Rest-Methoden als JSON zurückgegeben wird
public class RoomRestController {

    private final RoomService roomService;

    public RoomRestController(RoomService roomService) {
        this.roomService = roomService;
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
    public ResponseEntity<Void> addItem(@RequestBody Item item){
    roomService.addItemToRoom(item);
    return ResponseEntity.ok().build();
    }

    @GetMapping(path = "/api/v1/rooms/getitems/{id}")
    public ResponseEntity<List<Item>> getItems(@PathVariable Long id){
        var items = roomService.getAllItemsFromRoom(id);
        return items != null? ResponseEntity.ok(items):ResponseEntity.notFound().build();
    }

    @DeleteMapping(path = "/api/v1/rooms/{id}")
    public ResponseEntity<Void> deleteRooms(@PathVariable Long id){
        boolean roomFound = roomService.deleteRoom(id);
        return roomFound ? ResponseEntity.ok().build(): ResponseEntity.notFound().build();
    }

    @PutMapping(path = "/api/v1/{roomid}/users/{userid}")
    public ResponseEntity<Void> enroledUser(@PathVariable Long roomid, @PathVariable Long userid){
        boolean success = roomService.addUserToRoom(roomid,userid);
        return success ? ResponseEntity.ok().build() : ResponseEntity.notFound().build();
    }

}
