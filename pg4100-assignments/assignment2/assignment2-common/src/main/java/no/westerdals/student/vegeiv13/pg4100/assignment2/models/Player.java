package no.westerdals.student.vegeiv13.pg4100.assignment2.models;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import java.io.Serializable;

@Entity
public class Player implements Serializable {

    private static final long serialVersionUID = 5L;

    @Id
    @GeneratedValue
    private Long id;

    private String name;

    public Player() {

    }

    public Player(final String name) {
        this.name = name;
    }

    public static boolean verify(final Player player) {
        if (player.getName() == null || player.getName().replaceAll(" ", "").equals("")) {
            return false;
        } else {
            return true;
        }
    }

    @Override
    public String toString() {
        return "Player{" +
                "id=" + id +
                ", name='" + name + '\'' +
                '}';
    }

    public Long getId() {
        return id;
    }

    public void setId(final Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(final String name) {
        this.name = name;
    }
}
