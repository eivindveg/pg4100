package loesninger;

import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

public class Felles {
    private boolean oppdaterMinutt = false;
    private ReentrantLock lock;
    private Condition nyttMinutt;

    public Felles() {
        this.lock = new ReentrantLock();
        nyttMinutt = lock.newCondition();
    }

    public void vent() {
        lock.lock();
        try {
            while (!oppdaterMinutt) {
                nyttMinutt.await();
            }
            oppdaterMinutt = false;
        } catch (InterruptedException e) {
        } finally {
            lock.unlock();
        }
    }

    public void minuttTikk() {
        lock.lock();
        try {
            oppdaterMinutt = true;
            nyttMinutt.signal();
        } finally {
            lock.unlock();
        }
    }
}
