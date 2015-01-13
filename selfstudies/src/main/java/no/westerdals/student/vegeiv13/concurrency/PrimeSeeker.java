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
        if(value <= 3) {
            return value > 1;
        } else if(value % 2 == 0 || value % 3 == 0) {
            return false;
        } else {
            for(int i = 5; i * i <= value; i += 6) {
                if(value % i == 0 || value % (i + 2) == 0) {
                    return false;
                }
            }
            return true;
        }
    }

}
