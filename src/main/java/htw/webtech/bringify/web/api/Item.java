package htw.webtech.bringify.web.api;

import java.util.List;

public class Item {

    private Long id;
    private String name;
    private int ammount;
    private Long raumid;

    private String username;


    public Item(Long id,String name, int ammount, Long raumid,String username) {
        this.name = name;
        this.ammount = ammount;
        this.raumid = raumid;
        this.username =username;
        this.id = id;
    }

    public Item() {

    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getAmmount() {
        return ammount;
    }

    public void setAmmount(int ammount) {
        this.ammount = ammount;
    }

    public Long getRaumid() {
        return raumid;
    }

    public void setRaumid(Long raumid) {
        this.raumid = raumid;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }
}
