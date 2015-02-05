package no.westerdals.student.vegeiv13.concurrency;

import java.util.concurrent.Callable;

public class PrimeSeeker implements Callable<Long> {

    private long value;

    public PrimeSeeker(long n) {
        value = n;
    }

    @Override
    public Long call() throws Exception {
        if(checkPrime()) {
            return value;
        } else {
            return -1L;
        }
    }

    private boolean checkPrime() {
        if(value <= 3L) {
            return value > 1L;
        } else if(value % 2L == 0L || value % 3L == 0L) {
            return false;
        } else {
            for(long i = 5L; i * i <= value; i += 6L) {
                if(value % i == 0L || value % (i + 2L) == 0L) {
                    return false;
                }
            }
            return true;
        }
    }

}
