import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;

public class MonitorImproved {

    public static void main(final String[] args) throws ExecutionException, InterruptedException {
        final Random random = new Random();
        final List<Integer> data = new ArrayList<>();
        for (int i = 0; i < 1_000_0000_0; i++) {
            data.add(random.nextInt(0, 100));
        }

        final MonitorImproved monitor = new MonitorImproved(data);

        final double result = monitor.runAndMonitor();
        System.out.println("Result = " + result);
    }

    private final List<Integer> data;
    AtomicInteger nProcessed = new AtomicInteger(0);


    public MonitorImproved(List<Integer> data) {
        this.data = data;
    }

    public Double runAndMonitor() throws InterruptedException, ExecutionException {
        final ExecutorService pool = ForkJoinPool.commonPool();
        final TaskMonitor monitor = new TaskMonitor(10);

        final int chunkSize = data.size() / 4;
        final int remainder = data.size() % 4;

        final List<Future<Double>> futures = new ArrayList<>(chunkSize);

        // Submitting a task to a threadpool returns a future with type T.
        // However, our monitor task returns void so we used the wildcard operator `?` too allow this.
        // Java is only now starting to using the likes of `_` to represent a return value that we do not care about. Most
        // versions of Java oo not allow it.
        final Future<?> monitorResult = pool.submit(monitor);

        for (int i = 0; i < 4; i += 1) {
            final int low = i * chunkSize;
            final int high = (i + 1) * chunkSize;
            final TaskProcessor processor = new TaskProcessor(low, high);
            futures.add(pool.submit(processor));
        }

        if (remainder > 0) {
            futures.add(pool.submit(new TaskProcessor(data.size() - remainder, data.size())));
        }

        double total = 0.0;
        for (final Future<Double> result : futures) {
            final double r = result.get();
            total += r;
        }

        monitor.setKeepRunning(false);
        pool.shutdown();

        return total;
    }

    private class TaskMonitor implements Runnable {

        private long intervalMillis = 100;

        private boolean keepRunning = true;

        public TaskMonitor() {
        }

        public void setKeepRunning(boolean keepRunning) {
            this.keepRunning = keepRunning;
        }

        public TaskMonitor(final long intervalMillis) {
            this.intervalMillis = intervalMillis;
        }


        @Override
        public void run() {
            while (this.keepRunning) {
                System.out.printf("Thread %d | Processed [%d/%d]\n", Thread.currentThread().threadId(), nProcessed.get(), data.size());
                try {
                    Thread.sleep(this.intervalMillis);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }

        }
    }

    private class TaskProcessor implements Callable<Double> {

        private final int low;
        private final int high;
        private int nProc;

        public TaskProcessor(final int low, final int high) {
            this.low = low;
            this.high = high;
        }

        @Override
        public Double call() {
            double runningTotal = 0.0;
            for (int i = low; i < high; i++) {
                runningTotal += data.get(i);
                nProcessed.getAndAdd(1);
                nProc += 1;
            }
            System.out.println("Thread " + Thread.currentThread().threadId() + " finished. Processed " + nProc + " items");
            return runningTotal;
        }
    }

}
