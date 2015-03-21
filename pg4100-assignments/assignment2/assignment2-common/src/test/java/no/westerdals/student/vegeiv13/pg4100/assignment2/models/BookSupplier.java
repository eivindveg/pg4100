package no.westerdals.student.vegeiv13.pg4100.assignment2.models;

public class BookSupplier {
    
    public Book getBook() {
        final Book book = new Book();
        book.setAuthor(BookTest.TEST_AUTHOR);
        book.setId(Long.MAX_VALUE);
        book.setIsbn(BookTest.ISBN);
        book.setPages(BookTest.PAGES);
        book.setReleased(BookTest.YEAR_2000);
        book.setTitle(BookTest.TEST_TITLE);
        return book;
    }
}
