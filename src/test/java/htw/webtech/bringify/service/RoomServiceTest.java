package htw.webtech.bringify.service;

import com.sun.xml.bind.v2.TODO;
import htw.webtech.bringify.persistence.*;
import htw.webtech.bringify.web.api.*;
import org.assertj.core.api.WithAssertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.*;

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


        var roomEntity2 = Mockito.mock(RoomEntity.class);
        doReturn(2L).when(roomEntity2).getId();
        doReturn(1L).when(roomEntity2).getOwner();
        doReturn("Raum2").when(roomEntity2).getRoomName();
        doReturn("1234").when(roomEntity2).getKeyword();
        doReturn("Das ist Raum 2").when(roomEntity2).getBeschreibung();
        doReturn(null).when(roomEntity2).getItems();


        List<RoomEntity> roomEntityList = new ArrayList<>();
        roomEntityList.add(roomEntity1);
        roomEntityList.add(roomEntity2);

        doReturn(roomEntityList).when(roomRepository).findAll();

        Room room1 = new Room(1,"Raum1", "1234","Das ist Raum 1",1,new ArrayList<>());
        Room room2 = new Room(2,"Raum2", "1234","Das ist Raum 2",1,new ArrayList<>());
        List<Room> roomListExpected = new ArrayList<>();
        roomListExpected.add(room1);
        roomListExpected.add(room2);

        //when
        List<Room> result = roomService.findAll();

        //then
        assertThat(result.get(0).getId()).isEqualTo(room1.getId());
        assertThat(result.get(0).getRoomName()).isEqualTo(room1.getRoomName());
        assertThat(result.get(0).getOwner()).isEqualTo(room1.getOwner());
        assertThat(result.get(0).getBeschreibung()).isEqualTo(room1.getBeschreibung());
        assertThat(result.get(0).getKeyword()).isEqualTo(room1.getKeyword());
        assertThat(result.get(0).getItem()).isEqualTo(room1.getItem());

        assertThat(result.get(1).getId()).isEqualTo(room2.getId());
        assertThat(result.get(1).getRoomName()).isEqualTo(room2.getRoomName());
        assertThat(result.get(1).getOwner()).isEqualTo(room2.getOwner());
        assertThat(result.get(1).getBeschreibung()).isEqualTo(room2.getBeschreibung());
        assertThat(result.get(1).getKeyword()).isEqualTo(room2.getKeyword());
        assertThat(result.get(1).getItem()).isEqualTo(room2.getItem());
    }
    @Test
    @DisplayName("should return all items from a room")
    void get_all_items_from_room(){
        //given
        Long raumIdGiven = 1L;

        Set<ItemEntity> itemEntityList = new HashSet<>();
        itemEntityList.add(new ItemEntity("Banane", 2, new RoomEntity("Raum", "1234", "Beschreibung", 1, null,null),null));

        var roomEntityGiven = Mockito.mock(RoomEntity.class);
        doReturn(itemEntityList).when(roomEntityGiven).getItems();

        doReturn(Optional.of(roomEntityGiven)).when(roomRepository).findById(raumIdGiven);


        List<Item> expected = new ArrayList<>();
        expected.add(new Item(1L,"Banane", 2, 1L,"Peter"));
        expected.add(new Item(2L,"Kirsche", 2, 1L,"Paul"));

        //when
        var result = roomService.getAllItemsFromRoom(raumIdGiven);

        //then


        List<Item> resultList = new ArrayList<>();
        for(Item i: result){
            resultList.add(i);
        }
        assertThat(resultList.get(0).getName()).isEqualTo(expected.get(0).getName());
        assertThat(resultList.get(0).getAmmount()).isEqualTo(expected.get(0).getAmmount());


    }
    @Test
    @DisplayName("should return user from room")
    void get_all_user_from_room(){
        Long raumIdGiven = 1L;

        Set<UserEntity> userEntitySet = new HashSet<>();
        userEntitySet.add(new UserEntity("Julia", "julia@gmx.de", "1234"));

        var roomEntityGiven = Mockito.mock(RoomEntity.class);
        doReturn(userEntitySet).when(roomEntityGiven).getUsers();

        doReturn(Optional.of(roomEntityGiven)).when(roomRepository).findById(raumIdGiven);

        List<User> expected = new ArrayList<>();
        expected.add(new User(1L,"Julia", "julia@gmx.de", "1234"));



        //when
        var result = roomService.getAllUserFromRoom(raumIdGiven);

        //then
        assertThat(result.get(0).getUsername()).isEqualTo(expected.get(0).getUsername());
        assertThat(result.get(0).getMail()).isEqualTo(expected.get(0).getMail());
        assertThat(result.get(0).getPassword()).isEqualTo(expected.get(0).getPassword());




    }
    @Test
    @DisplayName("should the Room wth the given Id")
    void should_find_room_by_id(){
        //given
        Long givenId = 1L;
        Optional<RoomEntity> room = Optional.of(new RoomEntity("raum1", "1234", "beschreibung", 1, null,null));
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
    @DisplayName("should convert a RoomEntity in a Room")
    void roomEntity_to_Room(){
        //given
        var roomEntityGiven = Mockito.mock(RoomEntity.class);
        doReturn(1L).when(roomEntityGiven).getId();
        doReturn("Raum").when(roomEntityGiven).getRoomName();
        doReturn("1234").when(roomEntityGiven).getKeyword();
        doReturn("Beschreibung").when(roomEntityGiven).getBeschreibung();
        doReturn(1L).when(roomEntityGiven).getOwner();
        doReturn(null).when(roomEntityGiven).getItems();


        Room expected = new Room(1L, "Raum", "1234", "Beschreibung",1L,new ArrayList<>());

        //when
        var result = roomService.roomEntityToRoom(roomEntityGiven);

        //then
        assertThat(result.getId()).isEqualTo(expected.getId());
    }
    @Test
    @DisplayName("should return false if room not present")
    void add_user_to_room(){
        //given
        Long roomIdGiven = 1L;
        Username usernameGiven = new Username("Tom");

        Set<UserEntity> userEntityList = new HashSet<>();
        userEntityList.add(new UserEntity("Tom", "tom@gmx.de", "1234"));
        //userEntityList.add(new UserEntity("Tom2", "tom2@gmx.de", "1234"));

        var roomEntityGiven = Mockito.mock(RoomEntity.class);
        //doReturn(userEntityList).when(roomEntityGiven).getUsers();



        Optional<RoomEntity> roomOptional = Optional.of(roomEntityGiven);
        //doReturn(null).when(userRepository).findById(roomIdGiven);

        Optional<UserEntity> userOptional = Optional.of(new UserEntity("Tom2", "tom2@gmx.de", "1234"));
        doReturn(userOptional).when(userRepository).findByUsername(usernameGiven.getUsername());

        RoomEntity roomEntity = new RoomEntity("Room", "1234", "Beschreibung", 1L, null,null);



        //when
        var result = roomService.addUserToRoom(roomIdGiven, usernameGiven);

        //then
        assertThat(result).isFalse();
    }

    @Test
    @DisplayName("should delete Room with id")
    void delete_room(){
        //given
        Long roomIdGiven = 1L;
        doReturn(true).when(roomRepository).existsById(roomIdGiven);
        doNothing().when(roomRepository).deleteById(roomIdGiven);

        //when
        var result = roomService.deleteRoom(roomIdGiven);

        //then
        assertThat(result).isTrue();
    }


    @Test
    @DisplayName("should create a room with manipulationRequest")
    void get_description(){
        Long givenId = 1L;
        RoomEntity room = new RoomEntity("Raum", "1234", "Beschreibung", 1L, null,null);
        doReturn(Optional.of(room)).when(roomRepository).findById(givenId);

        //when
        var result = roomService.getDescription(givenId);

        //then
        assertThat(result.getDescription()).isEqualTo(room.getBeschreibung());
    }







}
