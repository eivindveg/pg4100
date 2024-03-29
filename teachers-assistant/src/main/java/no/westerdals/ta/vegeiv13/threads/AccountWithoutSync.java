package no.westerdals.ta.vegeiv13.threads;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class AccountWithoutSync {
    private static Account account = new Account();

    public static void main(String[] args) {
        ExecutorService executor = Executors.newCachedThreadPool();

        // Create and launch 100 threads
        for (int i = 0; i < 100; i++) {
            executor.execute(new AddAPennyTask());
        }

        executor.shutdown();

        // Wait until all tasks are finished
        while (!executor.isTerminated()) {
        }

        System.out.println("What is balance? " + account.getBalance());
    }

    // A thread for adding a penny to the account
    private static class AddAPennyTask implements Runnable {
        public void run() {
            account.deposit(1);
        }
    }

    // An inner class for account
    private static class Account {

        // Simplistic model
        private static final Object THREAD_LOCK = new Object();

        private int balance = 0;

        public int getBalance() {
            return balance;
        }

        public void deposit(int amount) {
            synchronized (THREAD_LOCK) {
                int newBalance = getBalance() + amount;

                try {
                    Thread.sleep(5);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                balance = newBalance;
            }
        }
    }
}
