package yubo.v1.java7threadcookbook.ch02;

import java.util.Objects;

public class Bank implements Runnable {
    private Account account;

    public Bank(final Account account) {
        Objects.nonNull(account);
        this.account = account;
    }

    @Override
    public void run() {
        for (int i = 0; i < 10; i++) {
            System.out.println("running in Bank");
            account.substractAmount(1000);
        }
    }
}
