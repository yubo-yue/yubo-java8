package yubo.v1.java7threadcookbook.ch03.latch;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

public class VideoConference implements Runnable {
    private final CountDownLatch controller;

    public VideoConference(int number) {
        this.controller = new CountDownLatch(number);
    }

    @Override
    public void run() {
        System.out.printf("VideoConference: Initialization: %d participants.\n", controller.getCount());
        try {
            controller.await();
            System.out.printf("VideoConference: All the participants have come\n");
            System.out.printf("VideoConference: Let's start...\n");
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public void arrive(final String name) {
        System.out.printf("%s has arrived \n", name);
        controller.countDown();

        System.out.printf("VideoConference: Waiting for %d participants.\n", controller.getCount());
    }

    public static void main(String[] args) {
        VideoConference conference = new VideoConference(5);
        Thread threadConference = new Thread(conference);
        threadConference.start();

        for (int i = 0; i < 5; i++) {
            Participant p = new Participant(conference, "Participant " + i);
            Thread t = new Thread(p);
            t.start();
        }
    }
}

class Participant implements Runnable {
    private final VideoConference conference;
    private final String name;

    public Participant(final VideoConference conference, final String name) {
        this.conference = conference;
        this.name = name;
    }

    @Override
    public void run() {
        long duration = (long) (Math.random() * 10);
        try {
            TimeUnit.SECONDS.sleep(duration);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        conference.arrive(name);
    }
}
