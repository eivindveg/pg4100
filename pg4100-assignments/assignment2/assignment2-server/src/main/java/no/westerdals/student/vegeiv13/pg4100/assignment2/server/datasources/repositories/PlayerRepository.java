package no.westerdals.student.vegeiv13.pg4100.assignment2.server.datasources.repositories;

import no.westerdals.student.vegeiv13.pg4100.assignment2.models.Player;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PlayerRepository extends JpaRepository<Player, Long> {

    public Player findByName(final String name);

}
