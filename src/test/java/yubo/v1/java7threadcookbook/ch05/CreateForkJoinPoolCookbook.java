package yubo.v1.java7threadcookbook.ch05;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.RecursiveAction;
import java.util.concurrent.TimeUnit;

public class CreateForkJoinPoolCookbook {
    public static void main(String[] args) {
        final ProductListGenerator generator = new ProductListGenerator();
        final List<Product> products = generator.generateProducts();

        final Task task =  new Task(0, products.size(), products, 0.2f);
        ForkJoinPool forkJoinPool = new ForkJoinPool();

        forkJoinPool.execute(task);

        do {
            System.out.printf("Main: Thread count: %d%n", forkJoinPool.getActiveThreadCount());
            System.out.printf("Main: Thread steal: %d%n", forkJoinPool.getStealCount());
            System.out.printf("Main: Parallelism: %d%n", forkJoinPool.getParallelism());

            try {
                TimeUnit.MILLISECONDS.sleep(5);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        } while (!task.isDone());

        forkJoinPool.shutdown();

        if (task.isCompletedNormally()) {
            System.out.printf("Main: the process has completed normally %n");
        }
        for (int i = 0; i < products.size(); i++) {
            final Product product = products.get(i);
            if (product.getPrice() != 12) {
                System.out.printf("Product %s: %f%n", product.getName(), product.getPrice());
            }
        }
        System.out.printf("Main: End of the program%n");
    }

    @Data(staticConstructor = "of")
    private static class Product {
        private final String name;
        @NonNull
        private double price;
    }

    private static class ProductListGenerator {
        public List<Product> generateProducts() {
            final List<Product> products = new ArrayList<>();
            for (int i = 0; i < 1000; i++) {
                final Product product = Product.of(String.format("Product-%d", i), 1.0);
                products.add(product);
            }

            return products;
        }
    }

    @AllArgsConstructor
    private static class Task extends RecursiveAction {
        private int start;
        private int end;
        private List<Product> products;
        private double increment;

        @Override
        protected void compute() {
            if (end - start < 10) {
                updatePrices();
            } else {
                int middle = (start + end) / 2;
                System.out.printf("Task: Pending tasks: %s%n", getQueuedTaskCount());
                Task t1 = new Task(start, middle, products, increment);
                Task t2 = new Task(middle + 1, end, products, increment);
                invokeAll(t1, t2);
            }
        }

        private void updatePrices() {
            for (int i = start; i < end; i++) {
                Product product = products.get(i);
                product.setPrice(product.getPrice() * increment);
            }
        }
    }
}
