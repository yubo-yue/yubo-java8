package yubo.v1.java7threadcookbook.ch02;

public class Main {
    public static void main(String[] args) {
        final Account account = new Account();
        account.setBalance(1000);

        final Company company = new Company(account);
        final Bank bank = new Bank(account);

        System.out.printf("Account : Initial Balance: %f\n", account.
                getBalance());

        final Thread ct = new Thread(company);
        final Thread bt = new Thread(bank);

        ct.start();
        bt.start();

        try {
            ct.join();
            bt.join();
            System.out.printf("Account : Final Balance: %f\n", account.getBalance());
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
