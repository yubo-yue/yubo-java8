package yubo.v1.java7threadcookbook.ch04.executorcreation;

import java.util.concurrent.*;

public class CompletionServiceCookbook {
    public static void main(String[] args) {
        ExecutorService executorService = Executors.newCachedThreadPool();
        CompletionService<String> completionService = new ExecutorCompletionService<>(executorService);

        ReportRequest faceRequest = new ReportRequest("Face", completionService);
        ReportRequest onlineRequest = new ReportRequest("Online", completionService);
        Thread faceThread = new Thread(faceRequest);
        Thread onlineThread = new Thread(onlineRequest);
        ReportProcessor processor = new ReportProcessor(completionService);
        Thread senderThread = new Thread(processor);
        System.out.printf("Main: Starting the Threads\n");
        faceThread.start();
        onlineThread.start();
        senderThread.start();
        try {
            System.out.printf("Main: Waiting for the report generators.%n");
            faceThread.join();
            onlineThread.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        System.out.printf("Main: Shutting down the executor.\n");
        executorService.shutdown();
        try {
            executorService.awaitTermination(20, TimeUnit.SECONDS);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        processor.setEnd(true);
        System.out.println("Main: Ends");
    }

    private static class ReportGenerator implements Callable<String> {
        private String sender;
        private String title;

        public ReportGenerator(final String sender, final String title) {
            this.sender = sender;
            this.title = title;
        }

        @Override
        public String call() throws Exception {
            try {
                Long duration = (long) (Math.random() * 10);
                System.out.printf("%s_%s: ReportGenerator: Generating a report during % d seconds%n",
                        this.sender, this.title, duration);
                TimeUnit.SECONDS.sleep(duration);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            return String.format("%s_%s", sender, title);
        }
    }

    private static class ReportRequest implements Runnable {
        private String name;
        private CompletionService<String> service;

        public ReportRequest(final String name, final CompletionService<String> service) {
            this.name = name;
            this.service = service;
        }

        @Override
        public void run() {
            ReportGenerator reportGenerator = new ReportGenerator(name, "Report");
            service.submit(reportGenerator);
        }
    }

    private static class ReportProcessor implements Runnable {
        private CompletionService<String> service;
        private boolean end;

        public ReportProcessor(final CompletionService<String> service) {
            this.service = service;
            this.end = false;
        }

        @Override
        public void run() {
            while (!end) {
                try {
                    Future<String> result = service.poll(20, TimeUnit.SECONDS);
                    if (null != result) {
                        String report = result.get();
                        System.out.printf("ReportReceiver: Report received %s%n", report);
                    }
                } catch (InterruptedException | ExecutionException e) {
                    e.printStackTrace();
                }
            }
            System.out.printf("ReportSender: End%n");
        }

        public void setEnd(boolean end) {
            this.end = end;
        }
    }
}
