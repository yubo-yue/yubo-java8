package yubo.v1.java7threadcookbook.ch07;

import lombok.AllArgsConstructor;

import java.util.concurrent.atomic.AtomicInteger;

public class CustomizedAtomicObjectCookbook {
    private static class ParkingCounter extends AtomicInteger {
        private int maxNumber;

        public ParkingCounter(int maxNumber) {
            set(0);
            this.maxNumber = maxNumber;
        }

        public boolean carIn() {
            for (; ; ) {
                int value = get();
                if (value == maxNumber) {
                    System.out.printf("ParkingCounter: The parking lot is full%n");
                    return false;
                }
                int newValue = value + 1;
                boolean changed = compareAndSet(value, newValue);
                if (changed) {
                    System.out.printf("ParkingCounter: car in.%n");
                    return true;
                }
            }
        }

        public boolean carOut() {
            for (; ; ) {
                int value = get();
                if (value == 0) {
                    System.out.printf("ParkingCounter: The parking lot is empty%n");
                    return false;
                }

                int newValue = value - 1;
                boolean changed = compareAndSet(value, newValue);
                if (changed) {
                    System.out.printf("ParkingCounter: A car has gone out.%n");
                    return true;
                }
            }
        }
    }

    @AllArgsConstructor
    private static class Sensor1 implements Runnable {
        private final ParkingCounter parkingCounter;

        @Override
        public void run() {
            parkingCounter.carIn();
            parkingCounter.carIn();
            parkingCounter.carIn();
            parkingCounter.carIn();
            parkingCounter.carOut();
            parkingCounter.carOut();
            parkingCounter.carOut();
            parkingCounter.carIn();
            parkingCounter.carIn();
            parkingCounter.carIn();
        }
    }

    @AllArgsConstructor
    private static class Sensor2 implements Runnable {
        private final ParkingCounter parkingCounter;

        @Override
        public void run() {
            parkingCounter.carIn();
            parkingCounter.carOut();
            parkingCounter.carOut();
            parkingCounter.carIn();
            parkingCounter.carIn();
            parkingCounter.carIn();
            parkingCounter.carIn();
            parkingCounter.carIn();
            parkingCounter.carIn();
        }
    }

    public static void main(String[] args) throws InterruptedException {
        final ParkingCounter counter = new ParkingCounter(5);
        Sensor1 sensor1 = new Sensor1(counter);
        Sensor2 sensor2 = new Sensor2(counter);
        Thread thread1 = new Thread(sensor1);
        Thread thread2 = new Thread(sensor2);
        thread1.start();
        thread2.start();
        thread1.join();
        thread2.join();
        System.out.printf("Main: Number of cars: %d\n", counter.get());
        System.out.printf("Main: End of the program.\n");
    }
}
