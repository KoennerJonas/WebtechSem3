package htw.webtech.bringify.persistence;

import javax.persistence.*;


@Entity
@Table(name = "roles")
public class RolesEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    @Column
    private Roles name;

    public Roles getName() {
        return name;
    }
}