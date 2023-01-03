package htw.webtech.bringify.service;

import htw.webtech.bringify.persistence.ItemRepository;
import htw.webtech.bringify.persistence.RoomEntity;
import htw.webtech.bringify.persistence.RoomRepository;
import htw.webtech.bringify.persistence.UserRepository;
import org.assertj.core.api.WithAssertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;

@ExtendWith(MockitoExtension.class)
public class RoomServiceTest implements WithAssertions {

    @Mock
    private RoomRepository roomRepository;
    @Mock
    private UserRepository userRepository;
    @Mock
    private ItemRepository itemRepository;

    @Test
    @DisplayName("should return all rooms that exists")
    void should_return_all_rooms(){
        //given
        RoomEntity room1 = new RoomEntity("Room1","room123","This is a testroom",1,null);
        RoomEntity room2 = new RoomEntity("Room2","room123","This is a testroom",2,null);

        ArrayList<RoomEntity> expected = new ArrayList<>();
        expected.add(room1);
        expected.add(room2);
        roomRepository.save(room1);
        roomRepository.save(room2);

        //vergleich
        assertThat(roomRepository.findAll()).isEqualTo(expected);

    }



}
