package yubo.v1.java7threadcookbook.ch03.phaser;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Phaser;
import java.util.concurrent.TimeUnit;

public class PhaserDemo1 {
    public static void main(String[] args) throws InterruptedException {
        final List<Runnable> tasks = new ArrayList<>();

        for (int i = 0; i < 2; i++) {
            final Runnable task = () -> {
                int a = 0, b = 1;
                for (int i1 = 0; i1 < 2000000000; i1++) {
                    a = a + b;
                    b = a - b;
                }
                System.out.printf("[%-15s]running... %n", Thread.currentThread().getName());
            };

            tasks.add(task);
        }
        new PhaserDemo1().runTasks(tasks);
    }

    public void runTasks(final List<Runnable> tasks) throws InterruptedException {
        final Phaser phaser = new Phaser(1) {
            @Override
            protected boolean onAdvance(int phase, int registeredParties) {
                return phase >= 3 || registeredParties == 0;
            }
        };

        for (final Runnable task : tasks) {
            phaser.register();
            new Thread() {
                @Override
                public void run() {
                    do {
                        phaser.arriveAndAwaitAdvance();
                        task.run();
                    } while (!phaser.isTerminated());
                }
            }.start();

            TimeUnit.MICROSECONDS.sleep(500);
        }

        phaser.arriveAndDeregister();
    }
}
