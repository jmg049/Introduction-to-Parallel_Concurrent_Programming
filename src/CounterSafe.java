import java.util.concurrent.atomic.AtomicInteger;

public class CounterSafe {
    private static final AtomicInteger COUNTER = new AtomicInteger(0);

    private static final int ITERATIONS = 100_000;

    static void incrementSafe() {
        for (int i = 0; i < ITERATIONS; i++) {
            COUNTER.incrementAndGet();
        }
    }

    static void decrementSafe() {
        for (int i = 0; i < ITERATIONS; i++) {
            COUNTER.decrementAndGet();
        }
    }


    public static void main(final String[] args) throws InterruptedException {
        final Thread increment = new Thread(CounterSafe::incrementSafe);
        final Thread decrement = new Thread(CounterSafe::decrementSafe);

        System.out.println("Counter = " + COUNTER.get());
        increment.start();
        decrement.start();

        increment.join();
        decrement.join();
        System.out.println("Counter = " + COUNTER);
    }
}
