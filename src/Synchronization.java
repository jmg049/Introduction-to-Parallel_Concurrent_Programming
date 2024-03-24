import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.locks.ReentrantLock;

public class Synchronization {
    private static final ReentrantLock LOCK = new ReentrantLock();
    private static int COUNTER = 0;

    private static final int ITERATIONS = 100_000;

    private static void incrementSafe() {

        try {
            LOCK.lock();
            for (int i = 0; i < ITERATIONS; i++) {
                COUNTER += 1;
            }
        } finally {
            LOCK.unlock();
        }

    }

    private static void decrementSafe() {
        try {
            LOCK.lock();
            for (int i = 0; i < ITERATIONS; i++) {
                COUNTER -= 1;
            }
        } finally {
            LOCK.unlock();
        }
    }

     private synchronized static void increment() {
        for (int i = 0; i < ITERATIONS; i++) {
            COUNTER += 1;
        }
    }

     private synchronized static void decrement() {
        for (int i = 0; i < ITERATIONS; i++) {
            COUNTER -= 1;
        }
    }


    public static void main(final String[] args) throws InterruptedException {
        final Thread increment = new Thread(Synchronization::incrementSafe);
        final Thread decrement = new Thread(Synchronization::decrementSafe);

        System.out.println("Counter = " + COUNTER);
        increment.start();
        decrement.start();

        increment.join();
        decrement.join();
        System.out.println("Counter = " + COUNTER);

        COUNTER = 0;
        System.out.println("Counter = " + COUNTER);
        final Thread syncIncrement = new Thread(Synchronization::increment);
        final Thread syncDecrement = new Thread(Synchronization::decrement);
        syncIncrement.start();
        syncDecrement.start();

        syncIncrement.join();
        syncDecrement.join();
        System.out.println("Counter = " + COUNTER);
    }


}
