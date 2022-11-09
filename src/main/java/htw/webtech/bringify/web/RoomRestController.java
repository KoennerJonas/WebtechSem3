package htw.webtech.bringify.web;

import htw.webtech.bringify.service.RoomService;
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


    @PostMapping(path = "/api/v1/rooms")
    public ResponseEntity<Void> createRooms(@RequestBody RoomManipulationRequest request) throws URISyntaxException {
        var room =  roomService.createRoom(request);
        //URI ist die Adresse der neu erzeugten Ressource -> id sagt dann die Adresse zu einer bestimmten Resource
        URI uri = new URI("/api/v1/rooms/" + room.getId());
        return ResponseEntity.created(uri).build();
    }

    @PutMapping(path = "/api/v1/rooms/{id}")
    public ResponseEntity<Room> updateRooms(@PathVariable Long id, @RequestBody RoomManipulationRequest request){
        var room = roomService.updateRoom(id, request);
        return room != null ? ResponseEntity.ok(room) : ResponseEntity.notFound().build();
    }

    @DeleteMapping(path = "/api/v1/rooms/{id}")
    public boolean deleteRooms(Long id){
         return roomService.deleteRoom(id);
    }

}
