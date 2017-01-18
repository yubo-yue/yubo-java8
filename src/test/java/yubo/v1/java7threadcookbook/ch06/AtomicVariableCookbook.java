package yubo.v1.java7threadcookbook.ch06;

import lombok.AllArgsConstructor;

import java.util.concurrent.atomic.AtomicLong;

public class AtomicVariableCookbook {
    public static void main(String[] args) {
        final Account account = new Account();
        account.setBalance(1000L);
        System.out.printf("Account : Initial Balance: %d%n", account.getBalance());

        final Thread company = new Thread(new Company(account));
        final Thread bank = new Thread(new Bank(account));

        company.start();
        bank.start();

        try {
            company.join();
            bank.join();
            System.out.printf("Account : Final Balance: %d%n", account.getBalance());
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    private static class Account {
        private AtomicLong balance;

        public Account() {
            balance = new AtomicLong();
        }

        public long getBalance() {
            return balance.get();
        }

        public void setBalance(final long newBalance) {
            balance.set(newBalance);
        }

        public void addAmount(final long amount) {
            balance.getAndAdd(amount);
        }

        public void substractAmount(final long amount) {
            balance.getAndAdd(-amount);
        }
    }

    @AllArgsConstructor
    private static class Company implements Runnable {
        private Account account;

        @Override
        public void run() {
            for (int i = 0; i < 10; i++) {
                account.addAmount(1000L);
            }
        }
    }

    @AllArgsConstructor
    private static class Bank implements Runnable {
        private Account account;

        @Override
        public void run() {
            for (int i = 0; i < 10; i++) {
                account.substractAmount(1000);
            }
        }
    }
}
