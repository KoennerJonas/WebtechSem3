package htw.webtech.bringify.web.api;


import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class    Room {

    private long id;
    private String roomName;
    private String keyword;
    private String beschreibung;
    private long owner;
    private long members;

    private List<Long> itemIds;

    public Room(long id, String roomName, String keyword, String beschreibung, long owner, long members, List<Long> itemIds) {
        this.id = id;
        this.roomName = roomName;
        this.keyword = keyword;
        this.owner = owner;
        this.members = members;
        this.beschreibung = beschreibung;
        this.itemIds = itemIds;
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

    public String getBeschreibung() {
        return beschreibung;
    }

    public void setBeschreibung(String beschreibung) {
        this.beschreibung = beschreibung;
    }

    public List<Long> getItemIds() {
        return itemIds;
    }

    public void setItemIds(List<Long> itemIds) {
        this.itemIds = itemIds;
    }
}

