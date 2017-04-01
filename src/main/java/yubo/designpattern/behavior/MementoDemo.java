package yubo.designpattern.behavior;

import lombok.AllArgsConstructor;

public class MementoDemo {
    private interface PreviousCalculationToCareTaker {

    }

    private interface PreviousCalculationToOriginator {
        int getFirstNumber();

        int getSecondNumber();
    }

    @AllArgsConstructor
    private static class PreviousCalculationImpl implements PreviousCalculationToCareTaker, PreviousCalculationToOriginator {
        private int firstNumber;
        private int secondNumber;

        @Override
        public int getFirstNumber() {
            return firstNumber;
        }

        @Override
        public int getSecondNumber() {
            return secondNumber;
        }
    }

    private interface Calculator {
        PreviousCalculationToCareTaker backup();

        void restore(final PreviousCalculationToCareTaker lastCall);

        // Actual Services Provided by the originator
        int getCalculationResult();

        void setFirstNumber(int firstNumber);

        void setSecondNumber(int secondNumber);
    }

    private static class CalculatorImpl implements Calculator {
        private int firstNumber;
        private int secondNumber;

        @Override
        public PreviousCalculationToCareTaker backup() {
            return new PreviousCalculationImpl(firstNumber, secondNumber);
        }

        @Override
        public void restore(PreviousCalculationToCareTaker lastCall) {
            this.firstNumber = ((PreviousCalculationToOriginator) lastCall).getFirstNumber();
            this.secondNumber = ((PreviousCalculationToOriginator) lastCall).getSecondNumber();
        }

        @Override
        public int getCalculationResult() {
            return firstNumber + secondNumber;
        }

        @Override
        public void setFirstNumber(int firstNumber) {
            this.firstNumber = firstNumber;
        }

        @Override
        public void setSecondNumber(int secondNumber) {
            this.secondNumber = secondNumber;
        }
    }

    public static void main(String[] args) {
        //final Calculator calculator = new CalculatorImpl();
        Calculator c = new CalculatorImpl();
        c.setFirstNumber(1);
        c.setSecondNumber(12);

        System.out.println("1,12 result is : " + c.getCalculationResult());
        PreviousCalculationToCareTaker backup = c.backup();
        c.setFirstNumber(2);
        c.setSecondNumber(20);
        System.out.println("2,20 result is : " + c.getCalculationResult());

        c.restore(backup);
        System.out.println("1,12 result is : " + c.getCalculationResult());
    }
}
