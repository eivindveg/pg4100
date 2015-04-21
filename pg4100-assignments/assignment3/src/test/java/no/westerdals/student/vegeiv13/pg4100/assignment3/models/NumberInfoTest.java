package no.westerdals.student.vegeiv13.pg4100.assignment3.models;

import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleObjectProperty;
import no.westerdals.student.vegeiv13.pg4100.assignment3.TestUtils;
import no.westerdals.student.vegeiv13.pg4100.assignment3.concurrent.NumberInfoTask;
import org.junit.Before;
import org.junit.Test;

import java.math.BigInteger;
import java.util.concurrent.TimeUnit;

import static org.junit.Assert.*;

public class NumberInfoTest {

    private NumberInfo numberInfo;
    private BigInteger number;

    @Before
    public void setup() throws Exception {
        number = new BigInteger(TestUtils.DEFAULT_NUMBER);
        NumberInfoTask task = new NumberInfoTask(number);
        new Thread(task).start();
        numberInfo = task.get(20, TimeUnit.SECONDS);
        assertTrue("Our default value is a prime; tests for NumberInfo cannot succeed if not: correct tests",
                new BigInteger(TestUtils.DEFAULT_NUMBER).isProbablePrime(2));
    }

    @Test
    public void testIsPrime() {
        SimpleBooleanProperty booleanProperty = new SimpleBooleanProperty();
        booleanProperty.bind(numberInfo.isPrimeProperty());

        assertSame("Boolean property properly binds to value", booleanProperty.getValue(), numberInfo.isPrime());
    }

    @Test
    public void testNextPrime() {
        SimpleObjectProperty<BigInteger> numberProperty = new SimpleObjectProperty<>();
        assertNotNull("Next Prime is not null", numberInfo.getNextPrime());

        numberProperty.bind(numberInfo.nextPrimeProperty());
        assertEquals("NextPrime property properly binds to value", numberProperty.getValue(), numberInfo.getNextPrime());
    }

    @Test
    public void testGetValue() {
        SimpleObjectProperty<BigInteger> numberProperty = new SimpleObjectProperty<>(number);
        assertNotNull("Value is not null", numberInfo.getValue());

        numberProperty.bind(numberInfo.valueProperty());
        assertEquals("Value property properly binds to value", numberProperty.getValue(), numberInfo.getValue());
    }

    @Test
    public void testIsOdd() {
        SimpleBooleanProperty isOdd = new SimpleBooleanProperty();
        assertTrue("Value is odd(wouldn't be a prime if not(we don't count 2 in these tests))", numberInfo.isOdd());

        isOdd.bind(numberInfo.isOddProperty());
        assertSame("Boolean property properly binds to value", isOdd.getValue(), numberInfo.isOdd());
    }
}
