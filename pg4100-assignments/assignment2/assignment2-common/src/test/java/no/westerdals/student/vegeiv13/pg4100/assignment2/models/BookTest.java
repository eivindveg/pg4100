package no.westerdals.student.vegeiv13.pg4100.assignment2.models;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.junit.Before;
import org.junit.Test;

import javax.persistence.Entity;
import javax.persistence.Id;
import java.lang.reflect.Field;
import java.time.Year;
import java.util.IllegalFormatCodePointException;
import java.util.IllegalFormatWidthException;

import static org.junit.Assert.*;

public class BookTest {

    protected static final String TEST_AUTHOR = "TestAuthor";
    public static final String ISBN = "0123456789123";
    public static final int PAGES = 400;
    public static final Year YEAR_2000 = Year.of(2000);
    public static final String TEST_TITLE = "TestTitle";
    private Book book;

    @Before
    public void setup() {
        book = new BookSupplier().getBook();
    }

    @Test
    public void testClassIsValidJPA() {
        boolean annotationPresent = Book.class.isAnnotationPresent(Entity.class);
        assertTrue("Model class is annotated with @Entity", annotationPresent);
        boolean hasIdAnnotation = false;
        Field[] declaredFields = Book.class.getDeclaredFields();
        for (final Field declaredField : declaredFields) {
            if(declaredField.isAnnotationPresent(Id.class)) {
                hasIdAnnotation = true;
                break;
            }
        }
        assertTrue("Model class has an annotation for Id", hasIdAnnotation);
    }

    @Test(expected = IllegalFormatWidthException.class)
    public void testSetIsbnThrowsExceptionWithInvalidLength() throws Exception {
        book.setIsbn("012345");
        book.setIsbn("123123123123123");
    }

    @Test(expected = IllegalFormatCodePointException.class)
    public void testSetIsbnThrowsExceptionIfNotNumeral() {
        book.setIsbn("testIsbn");
    }

    @Test
    public void testSetIsbnHandlesNull() {
        book.setIsbn(null);
        String isbn = book.getIsbn();
        assertNull("Isbn is null after we set it to null", isbn);
    }

    @Test
    public void testStandardGetters() {
        String expected = TEST_AUTHOR;
        String actual = book.getAuthor();
        assertEquals(expected, actual);

        expected = TEST_TITLE;
        actual = book.getTitle();
        assertEquals(expected, actual);

        Long expectedLong = Long.MAX_VALUE;
        Long actualLong = book.getId();
        assertEquals(expectedLong, actualLong);

        expected = ISBN;
        actual = book.getIsbn();
        assertEquals(expected, actual);

        Integer expectedInt = PAGES;
        Integer actualInt = book.getPages();
        assertEquals(expectedInt, actualInt);

        Year expectedYear = YEAR_2000;
        Year actualYear = book.getReleased();
        assertSame(expectedYear, actualYear);

    }

    @Test
    public void testToString() {
        String expected = ToStringBuilder.reflectionToString(book);
        String actual = book.toString();

        assertEquals("Standard toString conforms to toString format", expected, actual);
    }

    @Test
    public void testSetReleasedWithInteger() {
        book.setReleased(2000);
        Year expected = YEAR_2000;
        Year actual = book.getReleased();

        assertEquals("Returned year after setting with integer is the same", expected, actual);
    }
}
