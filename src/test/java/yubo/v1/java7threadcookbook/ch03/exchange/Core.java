package yubo.v1.java7threadcookbook.ch03.exchange;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Exchanger;

public class Core {
    public static void main(String[] args) {
        final List<String> buffer1 = new ArrayList<>();
        final List<String> buffer2 = new ArrayList<>();

        final Exchanger<List<String>> exchanger = new Exchanger<>();

        final Producer producer = new Producer(buffer1, exchanger);
        final Consumer consumer = new Consumer(buffer2, exchanger);

        new Thread(producer).start();
        new Thread(consumer).start();
    }
}
