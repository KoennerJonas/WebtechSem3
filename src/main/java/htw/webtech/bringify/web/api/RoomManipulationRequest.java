package htw.webtech.bringify.web.api;

import java.util.List;

public class RoomManipulationRequest {
    //gleiche Klasse wie Room, bloß ohne id, da diese in der Datenbank erzeugt wird

    private String roomName;
    private String keyword;
    private String beschreibung;
    private long owner;
    private long members;
    private List<Long> items;

    public RoomManipulationRequest(String roomName, String keyword, String beschreibung, long owner, long members, List<Long> items) {
        this.roomName = roomName;
        this.keyword = keyword;
        this.owner = owner;
        this.members = members;
        this.items = items;
        this.beschreibung = beschreibung;
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
}
