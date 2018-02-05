package yubo.effectivejava.v3;

public enum PayrollDay {
    MONDAY,
    TUESDAY,
    WENDESDAY,
    THURSDAY,
    FRIDAY,
    SATURDAY(PayType.WEEKEND),
    SUNDAY(PayType.WEEKEND);

    private final PayType payType;

    int pay(int minutesWorked, int payRate) {
       return payType.pay(minutesWorked, payRate);
    }

    PayrollDay(final PayType payType) {
        this.payType = payType;
    }

    PayrollDay() {
        this(PayType.WORKDAY);
    }

    private enum PayType {
        WORKDAY {
            @Override
            int overtimeDay(final int mins, final int payRate) {
                return mins < MINS_PER_SHIFT ? 0
                        : (mins - MINS_PER_SHIFT) * payRate / 2;
            }
        },
        WEEKEND {
            @Override
            int overtimeDay(final int mins, final int payRate) {
                return mins * payRate / 2;
            }
        };

        private static final int MINS_PER_SHIFT = 60 * 8;

        abstract int overtimeDay(int mins, int payRate);

        int pay(int minsWorked, int payRate) {
            int basePay = minsWorked * payRate;

            return basePay + overtimeDay(minsWorked, payRate);
        }

    }

    public static void main(String[] args) {
        for (PayrollDay payrollDay : PayrollDay.values()) {
            System.out.printf("%s pay(%d, %d) = %d%n", payrollDay, 10, 20, payrollDay.pay(10, 20));
        }
    }
}
