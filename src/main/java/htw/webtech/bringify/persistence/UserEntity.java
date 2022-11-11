package htw.webtech.bringify.persistence;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Entity(name="users")
public class UserEntity {

    @ManyToMany(mappedBy = "users")
    private Set<RoomEntity> rooms = new HashSet<>();
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;


    @Column(nullable = false)
    private String username;

    @Column(nullable = false)
    private String mail;

    @Column(nullable = false)
    private String password;

    public UserEntity( String username, String mail, String password) {
        this.username = username;
        this.mail = mail;
        this.password = password;
    }

    public UserEntity() {

    }

    public Long getId() {
        return id;
    }


    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getMail() {

        return mail;
    }

    public void setMail(String mail) {
        this.mail = mail;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
