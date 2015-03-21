package no.westerdals.student.vegeiv13.pg4100.assignment2.quiz;

import no.westerdals.student.vegeiv13.pg4100.assignment2.models.Book;
import no.westerdals.student.vegeiv13.pg4100.assignment2.models.BookSupplier;
import no.westerdals.student.vegeiv13.pg4100.assignment2.models.Quiz;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertNotNull;

public class QuizGeneratorTest {

    private QuizGenerator generator;

    @Before
    public void setup() {
        generator = new QuizGenerator();
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testFromObjectThrowsExceptionWithInvalidClass() throws Exception {
        Object object = new Object();
        generator.fromObject(object);
    }

    @Test
    public void testGetQuizFromBook() throws Exception {
        Book book = new BookSupplier().getBook();

        Quiz quiz = generator.fromObject(book);
        assertNotNull("Quiz has a question", quiz.getQuestion());
        assertNotNull("Quiz has an answer", quiz.getAnswer());
    }
}
