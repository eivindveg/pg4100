package no.westerdals.student.vegeiv13.pg4100.assignment2.models;

import no.westerdals.student.vegeiv13.pg4100.assignment2.YearPersistenceConverter;
import no.westerdals.student.vegeiv13.pg4100.assignment2.quiz.annotations.QuizField;
import no.westerdals.student.vegeiv13.pg4100.assignment2.quiz.annotations.Quizzable;

import javax.persistence.*;
import java.time.Year;

@Entity
@Quizzable
public class Book {

    @Id
    @GeneratedValue
    private Long id;

    @Column(name = "ISBN", length = 20)
    private String isbn;
    @Column(length = 20)
    @QuizField(value = "Who wrote %i, published in %i?", identifiers = {"title", "released"})
    private String author;
    @Column(length = 30)
    @QuizField(value = "Author %i wrote this book in year %i, what is the title of this book?", identifiers = {"author", "released"})
    private String title;
    @Column(length = 4)
    private Integer pages;
    @QuizField(value = "In what year was the book %i, by %i released?", identifiers = {"title", "author"})
    @Convert(converter = YearPersistenceConverter.class)
    @Column(length = 4)
    private Year released;

    @Override
    public String toString() {
        return "Book{" +
                "id=" + id +
                ", isbn='" + isbn + '\'' +
                ", author='" + author + '\'' +
                ", title='" + title + '\'' +
                ", pages=" + pages +
                ", released=" + released +
                '}';
    }

    public Long getId() {
        return id;
    }

    public void setId(final Long id) {
        this.id = id;
    }

    public String getIsbn() {
        return isbn;
    }

    public void setIsbn(final String isbn) {
        this.isbn = isbn;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(final String author) {
        this.author = author;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(final String title) {
        this.title = title;
    }

    public Integer getPages() {
        return pages;
    }

    public void setPages(final Integer pages) {
        this.pages = pages;
    }

    public Year getReleased() {
        return released;
    }

    public void setReleased(final Year released) {
        this.released = released;
    }
}
