package no.westerdals.student.vegeiv13.pg4100.assignment2.server.datasources.repositories;

import no.westerdals.student.vegeiv13.pg4100.assignment2.models.Book;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BookRepository extends JpaRepository<Book, Long> {

    @Query("SELECT book from Book book ORDER BY RAND()")
    List<Book> findRandom();
}
