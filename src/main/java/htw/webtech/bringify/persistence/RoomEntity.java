package htw.webtech.bringify.persistence;

import htw.webtech.bringify.web.api.Item;
import htw.webtech.bringify.persistence.UserEntity;
import htw.webtech.bringify.web.api.User;

import javax.persistence.*;
import java.util.ArrayList;

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

    @Column(name = "owner", nullable = false)
    private long owner;

    @Column(name = "members")
    private long members;

    @Column(name = "Items")
    private long Items;

    public RoomEntity(long id, String roomName, String keyword, long owner, long members, long items) {
        this.id = id;
        this.roomName = roomName;
        this.keyword = keyword;
        this.owner = owner;
        this.members = members;
        Items = items;
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

    public long getMembers() {
        return members;
    }

    public void setMembers(long members) {
        this.members = members;
    }

    public long getItems() {
        return Items;
    }

    public void setItems(long items) {
        Items = items;
    }
}
