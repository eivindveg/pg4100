package no.westerdals.student.vegeiv13.pg4100.assignment2.server.datasources.repositories;

import no.westerdals.student.vegeiv13.pg4100.assignment2.models.Player;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PlayerRepository extends JpaRepository<Player, Long> {

    /**
     * Returns a player with the given name, if any
     * @param name name to search for
     * @return Player with given name, or null
     */
    public Player findByName(final String name);

}
