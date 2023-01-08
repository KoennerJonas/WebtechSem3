package htw.webtech.bringify.service;

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

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class UserServiceTest implements WithAssertions {


    @Mock
    private UserRepository userRepository;
    @Mock
    private RoomRepository roomRepository ;

    @InjectMocks
    private UserService userService;

    @Mock
    private RoomService roomService;

    @Test
    @DisplayName("should return all users")
    void should_return_all_users(){

        var users = List.of(new User(1L,"user1","email1@test.com","password1"),
                new User(2L,"user2","email2@test.com","password2"));

        var usersEntity = List.of(new UserEntity("user1","email1@test.com","password1"),
                new UserEntity("user2","email2@test.com","password2"));

        doReturn(usersEntity).when(userRepository).findAll();

        var result = userService.findAll();

        assertThat(result.get(0).getUsername()).isEqualTo(users.get(0).getUsername());
        assertThat(result.get(0).getMail()).isEqualTo(users.get(0).getMail());
        assertThat(result.get(0).getPassword()).isEqualTo(users.get(0).getPassword());
        assertThat(result.get(1).getUsername()).isEqualTo(users.get(1).getUsername());
        assertThat(result.get(1).getMail()).isEqualTo(users.get(1).getMail());
        assertThat(result.get(1).getPassword()).isEqualTo(users.get(1).getPassword());
    }

    @Test
    @DisplayName("should return user by id")
    void should_find_user_by_id(){
        //given
        Long givenId = 1L;
        Optional<UserEntity> user = Optional.of(new UserEntity("user", "test@gmail.com", "password"));
        doReturn(user).when(userRepository).findById(givenId);

        User expectedUser = new User(1L,"user","test@gmail.com","password");
        //when
        User result = userService.findById(givenId);

        //then
        assertThat(result.getUsername()).isEqualTo(expectedUser.getUsername());
        assertThat(result.getPassword()).isEqualTo(expectedUser.getPassword());
        assertThat(result.getMail()).isEqualTo(expectedUser.getMail());
    }

    @Test
    @DisplayName("should find user by email")
    void should_find_user_by_email(){
        //given
        String givenEmail = "test@gmail.com";
        Optional<UserEntity> user = Optional.of(new UserEntity("user", "test@gmail.com", "password"));
        doReturn(user).when(userRepository).findByMail(givenEmail);

        User expectedUser = new User(1L,"user","test@gmail.com","password");
        //when
        User result = userService.findByMail(givenEmail);

        //then
        assertThat(result.getUsername()).isEqualTo(expectedUser.getUsername());
        assertThat(result.getPassword()).isEqualTo(expectedUser.getPassword());
        assertThat(result.getMail()).isEqualTo(expectedUser.getMail());
    }

    @Test
    @DisplayName("should update the given user")
    void should_update_the_given_user(){
        Long givenId = 1L;
        UserManipulationRequest request = new UserManipulationRequest("neu", "test@gmail.com", "password");

        Optional<UserEntity> user = Optional.of(new UserEntity("alt", "test@gmail.com", "password"));
        doReturn(user).when(userRepository).findById(givenId);

        User result = userService.update(givenId,request);


        User expectedUser = new User(1L,"neu","test@gmail.com","password");

        assertThat(request.getUsername()).isEqualTo(expectedUser.getUsername());
    }


    @Test
    @DisplayName("should delete by id")
    void should_delete_by_id(){
        Long givenId = 1L;
        doReturn(true).when(userRepository).existsById(givenId);
        doNothing().when(userRepository).deleteById(givenId);
        var result = userService.deleteById(givenId);

        assertThat(result).isTrue();
    }
    @Test
    @DisplayName("should transform UserEntity to User")
    void should_transform_Userentity_to_User(){
        var userEntity = Mockito.mock(UserEntity.class);
        doReturn(1L).when(userEntity).getId();
        doReturn("user").when(userEntity).getUsername();
        doReturn("test@gmail.com").when(userEntity).getMail();
        doReturn("password").when(userEntity).getPassword();

        var result = userService.transformEntity(userEntity);

        User expectedUser = new User(1L,"user","test@gmail.com","password");

        assertThat(result.getId()).isEqualTo(expectedUser.getId());
        assertThat(result.getUsername()).isEqualTo(expectedUser.getUsername());
        assertThat(result.getPassword()).isEqualTo(expectedUser.getPassword());
        assertThat(result.getMail()).isEqualTo(expectedUser.getMail());

    }


}
