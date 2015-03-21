package no.westerdals.student.vegeiv13.pg4100.assignment2.server.datasources;

import no.westerdals.student.vegeiv13.pg4100.assignment2.models.Book;
import no.westerdals.student.vegeiv13.pg4100.assignment2.server.datasources.repositories.BookRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.List;

@Component
public class BookService {

    @Autowired
    private BookRepository bookRepository;

    @PostConstruct
    public void init() {
        if(bookRepository.findAll().isEmpty()) {
            List<Book> seed = new BookSeed();
            bookRepository.save(seed);
            bookRepository.flush();
        }
    }

    public Book getRandom() {
        List<Book> random = bookRepository.findRandom();
        if(random.isEmpty()) {
            return null;
        }
        return random.get(0);
    }
}
