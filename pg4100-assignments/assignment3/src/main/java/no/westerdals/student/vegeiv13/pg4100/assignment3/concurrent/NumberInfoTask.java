package no.westerdals.student.vegeiv13.pg4100.assignment3.concurrent;

import javafx.concurrent.Task;
import no.westerdals.student.vegeiv13.pg4100.assignment3.models.NumberInfo;

import java.math.BigInteger;

public class NumberInfoTask extends Task<NumberInfo> {

    private final BigInteger number;

    public NumberInfoTask(final BigInteger number) {
        this.number = number;
    }

    @Override
    protected NumberInfo call() throws Exception {
        boolean prime = number.isProbablePrime(100);
        BigInteger nextPrime = number.nextProbablePrime();
        boolean odd = !number.mod(new BigInteger("2")).equals(BigInteger.ZERO);

        return new NumberInfo(number, prime, nextPrime, odd);
    }
}
