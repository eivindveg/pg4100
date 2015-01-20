package no.westerdals.ta.vegeiv13.threads;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class ThreadCooperation {
    private static Account account = new Account();
    private static ExecutorService executor;

    public static void main(String[] args) {
        // Create a thread pool with 5 threads
        executor = Executors.newFixedThreadPool(5);
        DepositTask depositTask = new DepositTask();
        System.out.println("Thread 1\t\tThread 2\t\tBalance");
        executor.execute(depositTask);
        List<WithdrawTask> withdrawTasks = buildWithdrawTasks(4);

        executor.shutdown();
        try {
            executor.awaitTermination(10, TimeUnit.SECONDS);
        } catch (InterruptedException ignored) {
        }
        executor.shutdownNow();


        final int[] withdrawnSum = {0};
        System.out.println("Deposits: " + depositTask.getTotalDeposits());
        withdrawTasks.forEach(e -> {
            int withdrawn = e.getWithdrawn();
            withdrawnSum[0] +=  withdrawn;
        });
        System.out.println("Withdrawals: " + withdrawnSum[0]);
        System.out.println("Balance: " + account.getBalance());


    }

    private static List<WithdrawTask> buildWithdrawTasks(int tasks) {
        List<WithdrawTask> taskList = new ArrayList<>();
        for(int i = 0; i < tasks; i++) {
            WithdrawTask task = new WithdrawTask();
            executor.execute(task);
            taskList.add(task);
        }
        return taskList;
    }

    public static class DepositTask implements Runnable {

        public int getTotalDeposits() {
            return totalDeposits;
        }

        private int totalDeposits;

        @Override // Keep adding an amount to the account
        public void run() {
            try { // Purposely delay it to let the withdraw method proceed
                while (true) {
                    int amount = (int) (Math.random() * 10) + 1;
                    account.deposit(amount);
                    totalDeposits += amount;
                    Thread.sleep(1000);


                    // Dummy code. Keeps compiler warnings quiet
                    if (Thread.interrupted()) {
                        return;
                    }
                }
            } catch (InterruptedException ignored) {
            }
        }
    }

    public static class WithdrawTask implements Runnable {

        private int totalWithdrawn;

        @Override // Keep subtracting an amount from the account
        public void run() {
            try {
                while (true) {
                    Thread.sleep(1000);
                    int amount = (int) (Math.random() * 10) + 1;
                    account.withdraw(amount);
                    totalWithdrawn += amount;

                    if (Thread.interrupted()) {
                        break;
                    }
                }
            } catch(InterruptedException ignored) {
            }
        }

        public int getWithdrawn() {
            return totalWithdrawn;
        }
    }

    // An inner class for account
    private static class Account {
        // Create a new lock
        private static Lock lock = new ReentrantLock(true);

        // Create a condition
        private static Condition newDeposit = lock.newCondition();

        private int balance = 0;

        public int getBalance() {
            return balance;
        }

        public void withdraw(int amount) throws InterruptedException {
            lock.lock(); // Acquire the lock
            while (balance < amount) {
                System.out.println("\t\t\tWait for a deposit");
                newDeposit.await();
            }

            balance -= amount;
            System.out.println("\t\t\tWithdraw " + amount +
                    "\t\t" + getBalance());
        }

        public void deposit(int amount) {
            lock.lock(); // Acquire the lock
            try {
                balance += amount;
                System.out.println("Deposit " + amount +
                        "\t\t\t\t\t" + getBalance());

                // Signal thread waiting on the condition
                newDeposit.signalAll();
            } finally {
                lock.unlock(); // Release the lock
            }
        }
    }
}
