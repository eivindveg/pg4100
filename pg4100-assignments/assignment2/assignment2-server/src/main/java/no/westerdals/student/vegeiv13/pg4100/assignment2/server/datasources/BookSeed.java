package no.westerdals.student.vegeiv13.pg4100.assignment2.server.datasources;

import no.westerdals.student.vegeiv13.pg4100.assignment2.models.Book;

import java.time.Year;
import java.util.ArrayList;

public class BookSeed extends ArrayList<Book> {

    public BookSeed() {
        add(new Book("Gert Nygårdshaug", "Mengele Zoo", "9788202288495", 455, Year.of(2008)));
        add(new Book("Jared Diamond", "Guns, Germs and Steel", "0099302780", 480, Year.of(2005)));
        add(new Book("Daniel Kehlmann", "Oppmålingen av Verden", "9788205388390", 250, Year.of(2008)));
        add(new Book("Tomas Espedal", "Imot Kunsten", "9788205396166", 164, Year.of(2009)));
        add(new Book("J. R. R. Tolkien", "The Hobbit", "0048230707", 279, Year.of(1966)));
        add(new Book("Umberto Eco", "Rosens Navn", "8210027182", 551, Year.of(1985)));
        add(new Book("Margaret Atwood", "The Year of the Flood", "9781844085644", 518, Year.of(2010)));
        add(new Book("George R. R. Martin", "A Game of Thrones", "0553103547", 694, Year.of(1996)));
        add(new Book("George R. R. Martin", "A Clash of Kings", "0553108034", 768, Year.of(1999)));
    }
}
