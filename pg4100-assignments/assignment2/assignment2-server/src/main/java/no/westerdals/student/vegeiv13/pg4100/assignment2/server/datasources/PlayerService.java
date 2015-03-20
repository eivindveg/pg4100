package no.westerdals.student.vegeiv13.pg4100.assignment2.server.datasources;

import no.westerdals.student.vegeiv13.pg4100.assignment2.models.Player;
import no.westerdals.student.vegeiv13.pg4100.assignment2.server.datasources.repositories.PlayerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.transaction.Transactional;

@Component
public class PlayerService {

    @Autowired
    private PlayerRepository playerRepository;

    public Player findByName(final String name) {
        return playerRepository.findByName(name);
    }

    @Transactional
    public Player save(final Player player) {
        return playerRepository.saveAndFlush(player);
    }
}
