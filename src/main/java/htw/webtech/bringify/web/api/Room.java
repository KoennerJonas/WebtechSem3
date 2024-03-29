package htw.webtech.bringify.web.api;


import htw.webtech.bringify.persistence.UserEntity;

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

    private Set<UserEntity> users;

    private List<Long> item;

    public Room(long id, String roomName, String keyword, String beschreibung, long owner, List<Long> item) {
        this.id = id;
        this.roomName = roomName;
        this.keyword = keyword;
        this.owner = owner;
        this.beschreibung = beschreibung;
        this.item = item;
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


    public String getBeschreibung() {
        return beschreibung;
    }

    public void setBeschreibung(String beschreibung) {
        this.beschreibung = beschreibung;
    }

    public List<Long> getItem() {
        return item;
    }

    public void setItem(List<Long> item) {
        this.item = item;
    }

    public Set<UserEntity> getUsers() {
        return users;
    }

    public void setUsers(Set<UserEntity> users) {
        this.users = users;
    }
}

