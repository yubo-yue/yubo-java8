package yubo.v1.java7threadcookbook.ch06;

import lombok.AllArgsConstructor;
import lombok.NonNull;

import java.time.LocalDateTime;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;

public class BlockingThreadSafeListCookbook {
    public static void main(String[] args) throws InterruptedException {

        final LinkedBlockingQueue<String> list = new LinkedBlockingQueue<>(3);
        final Client client = new Client(list);
        final Thread t1 = new Thread(client);
        t1.start();

        for (int i = 0; i < 5; i++) {
            for (int j = 0; j < 3; j++) {
                final String request = list.take();
                System.out.printf("Main: Request %s at %s, Size %d%n", request, LocalDateTime.now(), list.size());
            }
            TimeUnit.SECONDS.sleep(2);
        }
    }

    @AllArgsConstructor
    private static class Client implements Runnable {
        @NonNull
        private final LinkedBlockingQueue<String> list;

        @Override
        public void run() {
            for (int i = 0; i < 3; i++) {
                for (int j = 0; j < 5; j++) {
                    final StringBuilder request = new StringBuilder();
                    request.append(i);
                    request.append(":");
                    request.append(j);
                    try {
                        list.put(request.toString());
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    System.out.printf("Client %s at %s%n", request, LocalDateTime.now());
                }
                try {
                    TimeUnit.SECONDS.sleep(1);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            System.out.printf("Client End.%n");
        }
    }
}
