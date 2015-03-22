package no.westerdals.student.vegeiv13.pg4100.assignment2;

import org.junit.Test;

import java.lang.reflect.Constructor;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class ConstantsTest {

    @Test
    public void testConstructorPrivate() throws Exception {
        Constructor<Constants> constructor = Constants.class.getDeclaredConstructor();
        assertFalse("Constructor not accessible", constructor.isAccessible());
        constructor.setAccessible(true);
        constructor.newInstance();
    }

    @SuppressWarnings({"PointlessBooleanExpression", "ConstantConditions"})
    @Test
    public void testPortValid() {
        boolean valid = Constants.PORT >= 1 && Constants.PORT <= 65535;
        assertTrue("Port number is valid", valid);
    }

    @SuppressWarnings({"PointlessBooleanExpression", "ConstantConditions"})
    @Test
    public void testTimeLimitValid() {
        boolean valid = Constants.TIME_LIMIT > 0;
        assertTrue("Port number is valid", valid);
    }
}
