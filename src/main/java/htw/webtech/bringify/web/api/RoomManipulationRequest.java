package htw.webtech.bringify.web.api;

import htw.webtech.bringify.persistence.UserEntity;

import java.util.List;
import java.util.Set;

public class RoomManipulationRequest {
    //gleiche Klasse wie Room, bloß ohne id, da diese in der Datenbank erzeugt wird

    private String roomName;
    private String keyword;
    private String beschreibung;
    private long owner;
    private long members;
    private List<Long> items;
    private Set<UserEntity> users;

    public RoomManipulationRequest(String roomName, String keyword, String beschreibung, long owner, List<Long> items, Set<UserEntity> users) {
        this.roomName = roomName;
        this.keyword = keyword;
        this.owner = owner;
        this.items = items;
        this.beschreibung = beschreibung;
        this.users = users;
    }

    public RoomManipulationRequest(){}

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

    public List<Long> getItems() {
        return items;
    }

    public void setItems(List<Long> items) {
        this.items = items;
    }

    public String getBeschreibung() {
        return beschreibung;
    }

    public void setBeschreibung(String beschreibung) {
        this.beschreibung = beschreibung;
    }

    public Set<UserEntity> getUsers() {
        return users;
    }

    public void setUsers(Set<UserEntity> users) {
        this.users = users;
    }
}
