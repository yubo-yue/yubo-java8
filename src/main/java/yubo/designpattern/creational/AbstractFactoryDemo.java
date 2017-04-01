package yubo.designpattern.creational;

public class AbstractFactoryDemo {
    private static abstract class AbstractProductA {
        public abstract void operationA1();

        public abstract void operationA2();
    }

    private static class ProductA1 extends AbstractProductA {
        @Override
        public void operationA1() {

        }

        @Override
        public void operationA2() {

        }
    }

    private static class ProductA2 extends AbstractProductA {
        @Override
        public void operationA1() {

        }

        @Override
        public void operationA2() {

        }
    }

    private static abstract class AbstractProductB {
        public abstract void operationB1();

        public abstract void operationB2();
    }

    private static class ProductB1 extends AbstractProductB {
        @Override
        public void operationB1() {

        }

        @Override
        public void operationB2() {

        }
    }

    private static class ProductB2 extends AbstractProductB {
        @Override
        public void operationB1() {

        }

        @Override
        public void operationB2() {

        }
    }

    private static abstract class AbstractFactory {
        abstract AbstractProductA createProductA();

        abstract AbstractProductB createProductB();
    }

    private static class ConcreateFactory1 extends AbstractFactory {
        @Override
        AbstractProductA createProductA() {
            return new ProductA1();
        }

        @Override
        AbstractProductB createProductB() {
            return new ProductB1();
        }
    }

    private static class ConcreteFactory2 extends AbstractFactory {

        @Override
        AbstractProductA createProductA() {
            return new ProductA2();
        }

        @Override
        AbstractProductB createProductB() {
            return new ProductB2();
        }
    }

    private static class FactoryMaker {
        private static AbstractFactory factory = null;

        static AbstractFactory getFactory(String choice) {
            if (choice.equals("a")) {
                return new ConcreateFactory1();
            } else {
                return new ConcreteFactory2();
            }
        }
    }

    public static void main(String[] args) {
        AbstractFactory pf = FactoryMaker.getFactory("a");
        pf.createProductA();
        pf.createProductB();
    }
}
