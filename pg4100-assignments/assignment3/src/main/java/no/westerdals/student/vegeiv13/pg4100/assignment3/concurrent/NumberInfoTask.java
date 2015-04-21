package no.westerdals.student.vegeiv13.pg4100.assignment3.concurrent;

import javafx.concurrent.Task;
import no.westerdals.student.vegeiv13.pg4100.assignment3.models.NumberInfo;

import java.math.BigInteger;

/**
 * A JavaFX worker task that calculates and sets up a {@link NumberInfo} for a given {@link BigInteger}
 *
 * @see NumberInfo
 * @see BigInteger#isProbablePrime(int)
 * @author Eivind Vegsundvåg
 * @since 1.0
 */
public class NumberInfoTask extends Task<NumberInfo> {

    private final BigInteger number;

    /**
     * Creates this task for the given number
     *
     * @param number any number
     */
    public NumberInfoTask(final BigInteger number) {
        this.number = number;
    }

    /**
     * Executes and builds the number on a {@link Thread}
     *
     * @return a constructed NumberInfo object
     * @throws Exception if any exception is encountered in the call, rethrown on the JavaFX thread when/if the value
     *                   is attempted retrieved
     * @see Task
     */
    @Override
    protected NumberInfo call() throws Exception {
        boolean prime = number.isProbablePrime(100);
        BigInteger nextPrime = number.nextProbablePrime();
        boolean odd = !number.mod(new BigInteger("2")).equals(BigInteger.ZERO);

        return new NumberInfo(number, prime, nextPrime, odd);
    }
}
