package yubo.v1.java7threadcookbook.ch02.independentattribute;

public class Main {

    public static void main(String[] args) {
        final Cinema cinema = new Cinema();
        final TicketOffice1 office1 = new TicketOffice1(cinema);
        final TicketOffice2 office2 = new TicketOffice2(cinema);

        Thread t1 = new Thread(office1);
        Thread t2 = new Thread(office2);

        t1.start();
        t2.start();

        try {
            t1.join();
            t2.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        System.out.printf("vacancies left in room1 %d\n", cinema.getVacanciesCinema1());
        System.out.printf("vacancies left in room2 %d\n", cinema.getVacanciesCinema2());
    }

}
