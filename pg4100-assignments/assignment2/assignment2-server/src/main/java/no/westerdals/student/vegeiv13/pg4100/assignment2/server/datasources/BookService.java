package no.westerdals.student.vegeiv13.pg4100.assignment2.server.datasources;

import no.westerdals.student.vegeiv13.pg4100.assignment2.models.Book;
import no.westerdals.student.vegeiv13.pg4100.assignment2.server.datasources.repositories.BookRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class BookService {

    @Autowired
    private BookRepository bookRepository;

    public Book getRandom() {
        return bookRepository.findRandom();
    }
}
