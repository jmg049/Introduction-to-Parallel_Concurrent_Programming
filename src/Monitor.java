import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.FutureTask;
import java.util.stream.IntStream;

public class Monitor {

    public static void main(final String[] args) throws ExecutionException, InterruptedException {
        final Random random = new Random();
        final List<Integer> data = new ArrayList<>();
        for (int i = 0; i < 1_000_0000; i++) {
            data.add(random.nextInt(0, 100));
        }

        final Monitor monitor = new Monitor(data);

        final double result = monitor.runAndMonitor();
        System.out.println("Result = " + result);
    }

    private final List<Integer> data;
    int nProcessed = 0;


    public Monitor(List<Integer> data) {
        this.data = data;
    }

    public Double runAndMonitor() throws InterruptedException, ExecutionException {

        final TaskProcessor processor = new TaskProcessor();
        final TaskMonitor monitor = new TaskMonitor();
        final FutureTask<Double> processorFuture = new FutureTask<>(processor);

        final Thread processorThread = new Thread(processorFuture);
        final Thread monitorThread = new Thread(monitor);

        processorThread.start();
        monitorThread.start();

        processorThread.join();
        monitor.setKeepRunning(false);
        monitorThread.join();
        return processorFuture.get();

    }

    private class TaskMonitor implements Runnable {

        private long intervalMillis = 100;

        private boolean keepRunning=  true;

        public TaskMonitor() {}

        public void setKeepRunning(boolean keepRunning) {
            this.keepRunning = keepRunning;
        }

        public TaskMonitor(final long intervalMillis) {
            this.intervalMillis = intervalMillis;
        }


        @Override
        public void run() {
            while (this.keepRunning) {
                System.out.printf("Processed [%d/%d]\n", nProcessed, data.size());
                try {
                    Thread.sleep(this.intervalMillis);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }

        }
    }

    private class TaskProcessor implements Callable<Double> {

        public TaskProcessor() {}

        @Override
        public Double call() throws Exception {
            double runningTotal = 0.0;
            for (Integer datum : data) {
                runningTotal += datum;
                Thread.sleep(1);
                nProcessed += 1;
            }
            return runningTotal;
        }
    }

}
