package no.westerdals.student.vegeiv13.pg4100.assignment2.models;

import org.apache.commons.lang3.builder.ToStringBuilder;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import java.io.Serializable;

/**
 * JPA entity representing a Player
 */
@Entity
public class Player implements Serializable {

    private static final long serialVersionUID = 5L;

    @Id
    @GeneratedValue
    private Long id;

    @Column(unique = true)
    private String name;
    private int score;

    public Player() {

    }

    public Player(final String name) {
        this.name = name;
    }

    /**
     * Checks if the given player object has a valid name
     *
     * @param player the player to check
     * @return whether or not the player's name is valid
     */
    public static boolean validate(final Player player) {
        return !(player.getName() == null || player.getName().replaceAll(" ", "").equals(""));
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

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .append("id", id)
                .append("name", name)
                .append("score", score)
                .toString();
    }

    public int getScore() {
        return score;
    }

    public void setScore(final int score) {
        this.score = score;
    }
}
