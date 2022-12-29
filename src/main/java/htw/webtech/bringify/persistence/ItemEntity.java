package htw.webtech.bringify.persistence;

import javax.persistence.*;

@Entity(name = "Item")
public class ItemEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private long id;

    @Column(name = "name")
    private String name;

    @Column(name = "ammount")
    private int ammount;

    @ManyToOne(cascade = CascadeType.MERGE)
    @JoinColumn(name = "raum_id", referencedColumnName = "id")
    private RoomEntity room;

    public ItemEntity(String name, int ammount, RoomEntity room) {
        this.name = name;
        this.ammount = ammount;
        this.room = room;
    }

    public long getId() {
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

    public RoomEntity getRoom() {
        return room;
    }

    public void setRoom(RoomEntity room) {
        this.room = room;
    }
}
