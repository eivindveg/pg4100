package no.westerdals.student.vegeiv13.pg4100.assignment2.server.datasources;

import no.westerdals.student.vegeiv13.pg4100.assignment2.models.Book;
import no.westerdals.student.vegeiv13.pg4100.assignment2.server.datasources.repositories.BookRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.List;

/**
 * Service object for talking to the database through a BookRepository
 */
@Component
public class BookService {

    private BookRepository bookRepository;

    @Autowired
    private Environment environment;

    @PostConstruct
    public void init() {
        if (environment.acceptsProfiles("test", "dev") && bookRepository.findAll().isEmpty()) {
            List<Book> seed = new BookSeed();
            bookRepository.save(seed);
            bookRepository.flush();
        }
    }

    /**
     * @return A random book from the database, or null if empty
     */
    public Book getRandom() {
        List<Book> random = bookRepository.findRandom();
        if (random.isEmpty()) {
            return null;
        }
        return random.get(0);
    }

    @Autowired
    public void setBookRepository(final BookRepository bookRepository) {
        this.bookRepository = bookRepository;
    }
}
