package yubo.v1.java7threadcookbook.ch06;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Map;
import java.util.concurrent.ConcurrentNavigableMap;
import java.util.concurrent.ConcurrentSkipListMap;

public class ThreadSafeNavigableMapCookbook {
    public static void main(String[] args) {
        final ConcurrentSkipListMap<String, Contact> map = new ConcurrentSkipListMap<>();
        final Thread[] threads = new Thread[25];
        int counter = 0;
        for (char c = 'A'; c < 'Z'; c++) {
            final Task task = new Task(map, String.valueOf(c));
            threads[counter] = new Thread(task);
            threads[counter++].start();
        }
        for (int i = 0; i < 25; i++) {
            try {
                threads[i].join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        System.out.printf("Main: Size of the map: %d\n", map.size());
        Map.Entry<String, Contact> element;
        Contact contact;

        element = map.firstEntry();
        contact = element.getValue();
        System.out.printf("Main: First Entry: %s: %s\n", contact.
                getName(), contact.getPhone());

        System.out.printf("Main: Submap from A1996 to B1002: \n");
        ConcurrentNavigableMap<String, Contact> subMap = map.subMap("A1996", "B1002");
        do {
            element = subMap.pollFirstEntry();
            if (element != null) {
                contact = element.getValue();
                System.out.printf("%s: %s\n", contact.getName(), contact.getPhone());
            }
        } while (element != null);
    }

    @AllArgsConstructor
    @Getter
    private static class Contact {
        private final String name;
        private final String phone;
    }

    @AllArgsConstructor
    private static class Task implements Runnable {
        private final ConcurrentSkipListMap<String, Contact> phoneBook;

        private final String id;

        @Override
        public void run() {
            for (int i = 0; i < 100; i++) {
                final Contact contact = new Contact(String.format("%s", id), String.format("%d", i + 1000));
                phoneBook.put(id + contact.getPhone(), contact);
            }
        }
    }
}
