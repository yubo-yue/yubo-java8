package yubo.v1.java7threadcookbook.ch02.independentattribute;

public class TicketOffice2 implements Runnable{
    private Cinema cinema;

    public TicketOffice2(final Cinema cinema) {
        this.cinema = cinema;
    }

    @Override
    public void run() {
        cinema.sellTicket2(2);
        cinema.sellTicket2(4);
        cinema.sellTicket1(2);
        cinema.sellTicket1(1);
        cinema.returnTicket2(2);
        cinema.sellTicket1(3);
        cinema.sellTicket2(2);
        cinema.sellTicket1(2);
    }
}
