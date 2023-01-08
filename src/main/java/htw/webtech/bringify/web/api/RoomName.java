package htw.webtech.bringify.web.api;

public class RoomName {
    String roomName;
    public RoomName(String roomName){
    this.roomName = roomName;
    }
    public RoomName(){

    }

    public String getRoomName() {
        return roomName;
    }

    public void setRoomName(String roomName) {
        this.roomName = roomName;
    }
}
