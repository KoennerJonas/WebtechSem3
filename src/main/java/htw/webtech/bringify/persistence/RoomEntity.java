package htw.webtech.bringify.persistence;

import htw.webtech.bringify.web.api.Item;
import htw.webtech.bringify.persistence.UserEntity;
import htw.webtech.bringify.web.api.User;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Entity(name = "rooms")
public class RoomEntity {
    //aus dieser Klasse wird SQL Code erstellt, wodurch die Tabelle Room und deren Spalte angelegt werden
    //Der Inhalt der Klasse doppelt sich teilweise zu dem des api package -> ist aber gewollt, da -> room gehört der Rest Schnitstelle und diese Klasse dient der reinen Abbildung als Tabelle

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private long id;

    @Column(name = "room_name", nullable = false)
    private String roomName;

    @Column(name = "keyword", nullable = false)
    private String keyword;
    @Column(name = "beschreibung", nullable = true)
    private String beschreibung;
    @Column(name = "owner", nullable = false)
    private long owner;

    @OneToMany(fetch = FetchType.EAGER)
    private List<ItemEntity> items = new ArrayList<>();


    @ManyToMany(fetch=FetchType.EAGER)
    @JoinTable(
            name="room_user",
            joinColumns = @JoinColumn(name = "room_id"),
            inverseJoinColumns = @JoinColumn(name = "user_id")
    )
    private Set<UserEntity> users = new HashSet<>();

    public RoomEntity(String roomName, String keyword, String beschreibung, long owner,  List items) {
        this.roomName = roomName;
        this.keyword = keyword;
        this.owner = owner;
        this.items = items;
        this.beschreibung = beschreibung;
    }

    protected RoomEntity() {

    }


    public long getId() {
        return id;
    }


    public String getRoomName() {
        return roomName;
    }

    public void setRoomName(String roomName) {
        this.roomName = roomName;
    }

    public String getKeyword() {
        return keyword;
    }

    public void setKeyword(String keyword) {
        this.keyword = keyword;
    }

    public long getOwner() {
        return owner;
    }

    public void setOwner(long owner) {
        this.owner = owner;
    }


    public List<ItemEntity> getItems() {
        return items;
    }

    public void setItems(List<ItemEntity> items) {
        this.items = items;
    }

    public Set<UserEntity> getUsers() {
        return users;
    }

    public void setUsers(Set<UserEntity> users) {
        this.users = users;
    }

    public String getBeschreibung() {
        return beschreibung;
    }

    public void setBeschreibung(String beschreibung) {
        this.beschreibung = beschreibung;
    }

}
