package htw.webtech.bringify.web.api;

import java.util.List;

public class Item {

    private String name;
    private int ammount;
    private Long raumid;

    public Item(String name, int ammount, Long raumid) {
        this.name = name;
        this.ammount = ammount;
        this.raumid = raumid;
    }

    public Item() {

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
}
