package htw.webtech.bringify.service;

import com.sun.xml.bind.v2.TODO;
import htw.webtech.bringify.persistence.*;
import htw.webtech.bringify.web.api.Item;
import htw.webtech.bringify.web.api.Room;
import htw.webtech.bringify.web.api.RoomManipulationRequest;
import org.assertj.core.api.WithAssertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class RoomServiceTest implements WithAssertions {

    @Mock
    private RoomRepository roomRepository;
    @Mock
    private UserRepository userRepository;
    @Mock
    private ItemRepository itemRepository;

    @InjectMocks
    private RoomService roomService;
    /*
    @Test
    @DisplayName("should return all rooms that exists")
    void should_return_all_rooms(){
        //given
        var roomEntity1 = Mockito.mock(RoomEntity.class);
        doReturn(1L).when(roomEntity1).getId();
        doReturn(1L).when(roomEntity1).getOwner();
        doReturn("Raum1").when(roomEntity1).getRoomName();
        doReturn("1234").when(roomEntity1).getKeyword();
        doReturn("Das ist Raum 1").when(roomEntity1).getBeschreibung();
        doReturn(null).when(roomEntity1).getItems();
        doReturn(null).when(roomEntity1).getUsers();

        var roomEntity2 = Mockito.mock(RoomEntity.class);
        doReturn(2L).when(roomEntity2).getId();
        doReturn(1L).when(roomEntity2).getOwner();
        doReturn("Raum2").when(roomEntity2).getRoomName();
        doReturn("1234").when(roomEntity2).getKeyword();
        doReturn("Das ist Raum 2").when(roomEntity2).getBeschreibung();
        doReturn(null).when(roomEntity2).getItems();
        doReturn(null).when(roomEntity2).getUsers();

        List<RoomEntity> roomEntityList = new ArrayList<>();
        roomEntityList.add(roomEntity1);
        roomEntityList.add(roomEntity2);

        doReturn(roomEntityList).when(roomRepository).findAll();

        Room room1 = new Room(1,"Raum1", "1234","Das ist Raum 1",1,null);
        Room room2 = new Room(2,"Raum2", "1234","Das ist Raum 2",1,null);
        List<Room> roomListExpected = new ArrayList<>();
        roomListExpected.add(room1);
        roomListExpected.add(room2);

        //when
        List<Room> result = roomService.findAll();

        //then
        assertThat(result.get(0).getRoomName()).isEqualTo(room1.getRoomName());
    }*/
    @Test
    @DisplayName("should the Room wth the given Id")
    void should_find_room_by_id(){
        //given
        Long givenId = 1L;
        Optional<RoomEntity> room = Optional.of(new RoomEntity("raum1", "1234", "beschreibung", 1, null));
        doReturn(room).when(roomRepository).findById(givenId);

        Room expectedRoom = new Room(1,"raum1","1234","beschreibung",1,null);
        //when
        Room result = roomService.findRoomByID(givenId);

        //then
        assertThat(result.getRoomName()).isEqualTo(expectedRoom.getRoomName());
        assertThat(result.getBeschreibung()).isEqualTo(expectedRoom.getBeschreibung());
        assertThat(result.getKeyword()).isEqualTo(expectedRoom.getKeyword());
        assertThat(result.getOwner()).isEqualTo(expectedRoom.getOwner());
    }

    //hier gibts das Problem, dass ich keine ID serten kann TEST ROT
    @Test
    @DisplayName("should create a new room from request")
    void should_ceate_a_new_room(){
        //given
        RoomManipulationRequest roomManipulationRequest = new RoomManipulationRequest("Raum1", "1234", "beschreibung", 1, null);
        RoomEntity repoSave = new RoomEntity("Raum1","1234","beschreibung",1,null);
        Room expectedRoom = new Room(1,"Raum1","1234","beschreibung",1,null);

        doReturn(repoSave).when(roomRepository).save(repoSave);
        doReturn(expectedRoom).when(roomService).roomEntityToRoom(repoSave);

        //when

        Room result = roomService.createRoom(roomManipulationRequest);

        //then
        assertThat(result.getRoomName()).isEqualTo(expectedRoom.getRoomName());
        assertThat(result.getKeyword()).isEqualTo(expectedRoom.getKeyword());
        assertThat(result.getBeschreibung()).isEqualTo(expectedRoom.getBeschreibung());
        assertThat(result.getOwner()).isEqualTo(expectedRoom.getOwner());
    }

    @Test
    @DisplayName("should delete a Room with Id")
    void should_delete_room_with_id(){
        Long givenId = 1L;
        doReturn(true).when(roomRepository).existsById(givenId);

        //when
        boolean result = roomService.deleteRoom(givenId);

        //then
        //verify(roomRepository).deleteById(givenId);
        assertThat(result).isTrue();
    }
    @Test
    @DisplayName("should update Room per manipulationRequest")
    void update_room(){

    }
    @Test
    @DisplayName("should add Item to Room")
    void add_items(){
        //given
        Item item = new Item("Kuchen", 3, 1L);
        RoomEntity roomEntity = new RoomEntity("Raum1", "1234", "beschreibung", 1,null);
        doReturn(roomEntity).when(roomRepository).findById(item.getRaumid()).get();

        //when

    }
    @Test
    @DisplayName("should get all items from Room")
    void get_all_items_from_Room(){
        //given
        Long givenId = 1L;

        RoomEntity roomEntity = new RoomEntity("Raum1", "1234", "beschreibung", 1,null);
        List<ItemEntity> itemEntityList = new ArrayList<>();
        ItemEntity itemEntity1;
        ItemEntity itemEntity2;



    }





}
