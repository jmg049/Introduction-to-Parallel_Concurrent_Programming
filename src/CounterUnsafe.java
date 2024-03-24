public class CounterUnsafe {
    private static volatile int COUNTER = 0;

    private static final int ITERATIONS = 100_000;

    private static void incrementUnsafe() {
        for (int i = 0; i < ITERATIONS; i++) {
            COUNTER += 1;
        }
    }

    private static void decrementUnsafe() {
        for (int i = 0; i < ITERATIONS; i++) {
            COUNTER -= 1;
        }
    }

    public static void main(final String[] args) throws InterruptedException {
        final Thread increment = new Thread(CounterUnsafe::incrementUnsafe);
        final Thread decrement = new Thread(CounterUnsafe::decrementUnsafe);

        System.out.println("Counter = " + COUNTER);
        increment.start();
        decrement.start();

        increment.join();
        decrement.join();
        System.out.println("Counter = " + COUNTER);
    }

}
