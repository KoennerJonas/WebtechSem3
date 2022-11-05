package htw.webtech.bringify.web.api;

public class RoomManipulationRequest {
    //gleiche Klasse wie Room, bloß ohne id, da diese in der Datenbank erzeugt wird

    private String roomName;
    private String keyword;
    private long owner;
    private long members;
    private long Items;

    public RoomManipulationRequest(String roomName, String keyword, long owner, long members, long items) {
        this.roomName = roomName;
        this.keyword = keyword;
        this.owner = owner;
        this.members = members;
        Items = items;
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
