package no.westerdals.student.vegeiv13.pg4100.assignment2.server.repositories;

import no.westerdals.student.vegeiv13.pg4100.assignment2.models.Book;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BookRepository extends CrudRepository <Book, Long> {
}