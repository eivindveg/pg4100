package no.westerdals.student.vegeiv13.pg4100.assignment3.concurrent;

import no.westerdals.student.vegeiv13.pg4100.assignment3.TestUtils;
import no.westerdals.student.vegeiv13.pg4100.assignment3.models.NumberInfo;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import rules.JavaFXThreadingRule;

import java.math.BigInteger;

import static org.junit.Assert.assertNotNull;

public class NumberInfoTaskTest {

    @Rule
    public JavaFXThreadingRule jfxRule = new JavaFXThreadingRule();

    private NumberInfoTask numberInfoTask;

    @Before
    public void setup() {
        numberInfoTask = new NumberInfoTask(new BigInteger(TestUtils.DEFAULT_NUMBER));
    }

    @Test
    public void testCall() throws Exception {
        Thread thread = new Thread(numberInfoTask);
        thread.start();
        thread.join(20000);
        NumberInfo numberInfo = numberInfoTask.get();
        assertNotNull("NumberInfoObject is not null on normal run", numberInfo);
    }
}
