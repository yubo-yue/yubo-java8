package yubo.designpattern.behavior;

import lombok.AllArgsConstructor;
import lombok.NonNull;

import java.util.ArrayDeque;
import java.util.Deque;

public class CommandDemo {
    private interface Order {
        void execute();
    }

    private static class StockTrade {
        public void buy() {
            System.out.println("you want to buy stock");
        }

        public void sell() {
            System.out.println("you want to sell stock");
        }

    }

    private static class Agent {
        private Deque<Order> orderQueue = new ArrayDeque<>();

        public void placeOrder(final Order order) {
            orderQueue.addLast(order);
            orderQueue.removeFirst().execute();
        }
    }

    @AllArgsConstructor
    private static class BuyStockOrder implements Order {
        @NonNull
        private final StockTrade stockTrade;

        @Override
        public void execute() {
            stockTrade.buy();
        }
    }

    @AllArgsConstructor
    private static class SellStockOrder implements Order {
        @NonNull
        private final StockTrade stockTrade;

        @Override
        public void execute() {
            stockTrade.sell();
        }
    }

    public static void main(String[] args) {
        final StockTrade receiver = new StockTrade();
        final BuyStockOrder buyCommand = new BuyStockOrder(receiver);
        final SellStockOrder sellCommand = new SellStockOrder(receiver);

        Agent agent = new Agent();
        agent.placeOrder(buyCommand);
        agent.placeOrder(sellCommand);

    }
}
