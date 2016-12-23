package yubo.v1.java7threadcookbook.ch02;

import java.util.concurrent.TimeUnit;

public class Account {
    private double balance;

    public synchronized void addAmount(double amount) {
        double tmp = balance;

        try {
            TimeUnit.SECONDS.sleep(1);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        tmp += amount;
        balance = tmp;
    }

    public synchronized void substractAmount(double amount) {
        double tmp = balance;

        try {
            TimeUnit.SECONDS.sleep(1);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        tmp -= amount;
        balance = tmp;
    }

    public double getBalance() {
        return balance;
    }

    public void setBalance(double balance) {
        this.balance = balance;
    }
}
