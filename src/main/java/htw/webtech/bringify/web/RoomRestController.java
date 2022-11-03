package htw.webtech.bringify.web;

import htw.webtech.bringify.web.api.Room;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

@RestController
public class RoomRestController {

    private List<Room> rooms;

    public RoomRestController(){
        rooms = new ArrayList<>();
        rooms.add(new Room("Party bei Jonas", "gurke", null, null, null ));
        rooms.add(new Room("Party bei Philipp", "Tomate", null, null, null ));
    }


    //Get
    @GetMapping(path = "/api/v1/roomnames")
    public ResponseEntity<List<Room>> fetchRooms(){
        return ResponseEntity.ok(rooms);
    }
}
