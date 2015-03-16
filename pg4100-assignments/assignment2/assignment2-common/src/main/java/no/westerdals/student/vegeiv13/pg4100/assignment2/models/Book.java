package no.westerdals.student.vegeiv13.pg4100.assignment2.models;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import java.time.Year;

@Entity
public class Book {

    @Id
    @GeneratedValue
    private Long id;

    @Column(name = "ISBN", length = 20)
    private String isbn;

    @Column(length = 20)
    private String author;

    @Column(length = 30)
    private String title;

    @Column(length = 4)
    private Integer pages;

    private Year released;

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
