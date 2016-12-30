package yubo.v1.java7threadcookbook.ch03.phaser;

import java.util.concurrent.Phaser;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicReferenceArray;

public class PhaserDemo {

    public static void main(String[] args) {
        final int workers = 2;
        final int workLength = 10;

        final Phaser phaser = new Phaser(workers + 1);
        final AtomicReferenceArray<String> lane1 = new AtomicReferenceArray<String>(new String[workLength]);
        final AtomicReferenceArray<String> lane2 = new AtomicReferenceArray<String>(new String[workLength]);

        new Thread("Producer 1") {
            @Override
            public void run() {
                for (int i = 0; i < workLength; i++) {
                    PhaserDemo.sleep(20);
                    lane1.set(i, "lane1-answer-" + i);
                    System.out.printf("[%-17s] working in lane1 finished phase [%d]%n",
                            Thread.currentThread().getName(), phaser.getPhase());

                    phaser.arriveAndAwaitAdvance();
                }
            }
        }.start();

        new Thread("Producer 2") {
            @Override
            public void run() {
                for (int i = 0; i < workLength; i++) {
                    PhaserDemo.sleep(40);
                    lane2.set(i, "lane2-answer-" + i);
                    System.out.printf("[%-17s] working in lane2 finished phase [%d]%n",
                            Thread.currentThread().getName(), phaser.getPhase());

                    phaser.arriveAndAwaitAdvance();
                }
            }
        }.start();

        new Thread("Slow Consumer") {
            @Override
            public void run() {
                for (int start = 0; start < workLength; ) {
                    System.out.printf("[%-17s] about to wait for phase [%d] to completion%n",
                            Thread.currentThread().getName(), start);

                    int phaseInProgress = phaser.awaitAdvance(start);

                    for (int i = start; i < phaseInProgress; i++) {
                        System.out.printf("[%-17s] read [%s] & [%s] from phase [%d]%n",
                                Thread.currentThread().getName(), lane1.get(i), lane2.get(i), i);
                    }

                    start = phaseInProgress;
                    PhaserDemo.sleep(90);
                }
            }
        }.start();

        phaser.arriveAndDeregister();
    }

    private static void sleep(int millis) {
        try {
            TimeUnit.MILLISECONDS.sleep(millis);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
