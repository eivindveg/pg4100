package no.westerdals.student.vegeiv13.pg4100.assignment2.server.datasources;

import no.westerdals.student.vegeiv13.pg4100.assignment2.models.Player;
import no.westerdals.student.vegeiv13.pg4100.assignment2.server.datasources.repositories.PlayerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.transaction.Transactional;

/**
 * Service object for saving and looking up Player objects from the database
 */
@Component
public class PlayerService {

    private PlayerRepository playerRepository;

    @Autowired
    public void setPlayerRepository(final PlayerRepository playerRepository) {
        this.playerRepository = playerRepository;
    }

    /**
     * Finds a player with the given name if any
     * @param name name to search for
     * @return Player with given name, or null
     */
    public Player findByName(final String name) {
        return playerRepository.findByName(name);
    }

    /**
     * Saves the given player
     * @param player player to save
     * @return the given Player
     */
    @Transactional
    public Player save(final Player player) {
        return playerRepository.saveAndFlush(player);
    }
}
