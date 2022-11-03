package htw.webtech.bringify.web.api;

import java.util.ArrayList;

public class Room {
    private String roomName;
    private String keyword;
    private User owner;
    private ArrayList<User> members;
    private ArrayList<Item> Items;

    public Room(String roomName, String keyword, User owner, ArrayList<User> members, ArrayList<Item> items) {
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

    public User getOwner() {
        return owner;
    }

    public void setOwner(User owner) {
        this.owner = owner;
    }

    public ArrayList<User> getMembers() {
        return members;
    }

    public void setMembers(ArrayList<User> members) {
        this.members = members;
    }

    public ArrayList<Item> getItems() {
        return Items;
    }

    public void setItems(ArrayList<Item> items) {
        Items = items;
    }
}
