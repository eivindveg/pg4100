package no.westerdals.student.vegeiv13.pg4100.assignment2.server.repositories;

public class RepositoryBundle {

    private BookRepository bookRepository;

    public RepositoryBundle(BookRepository bookRepository) {
        this.bookRepository = bookRepository;
    }

    public BookRepository getBookRepository() {
        return bookRepository;
    }

    public void setBookRepository(final BookRepository bookRepository) {
        this.bookRepository = bookRepository;
    }
}
