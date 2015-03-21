package no.westerdals.student.vegeiv13.pg4100.assignment2.models;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class QuizTest {

    public static final String A_TEST = "A test";
    public static final String WHAT_IS_THIS = "What is this?";
    private Quiz quiz;

    @Before
    public void setup() {
        quiz = new Quiz(WHAT_IS_THIS, A_TEST);
    }

    @Test
    public void testCloneNoAnswer() {
        Quiz quiz = this.quiz.cloneNoAnswer();
        assertNull("Quiz cloning strips the answer", quiz.getAnswer());

    }

    @Test
    public void testStandardGetters() {
        String expected = WHAT_IS_THIS;
        String actual = quiz.getQuestion();
        assertEquals(expected, actual);

        expected = A_TEST;
        actual = quiz.getAnswer();
        assertEquals(expected, actual);
    }

    @Test
    public void testEquals() {
        Quiz quiz = new Quiz(WHAT_IS_THIS, A_TEST);
        assertEquals("Equals matches", this.quiz, quiz);
    }

    @Test
    public void testHashCode() {
        Quiz quiz = new Quiz(WHAT_IS_THIS, A_TEST);
        int expected = this.quiz.hashCode();
        int actual = quiz.hashCode();
        assertEquals("HashCode for two equal objects is the same", expected, actual);

        quiz = new Quiz(WHAT_IS_THIS, "Not a test");
        actual = quiz.hashCode();
        assertNotEquals("HashCode for two unequal objects is not the same", expected, actual);
    }

    @Test
    public void testToString() {
        String expected = ToStringBuilder.reflectionToString(quiz);
        String actual = quiz.toString();

        assertEquals("Quiz class conforms to toString standard", expected, actual);
    }

}
