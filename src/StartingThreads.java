import java.util.concurrent.Callable;

public class StartingThreads {

    public static class CustomRunnable implements Runnable {
        @Override
        public void run() {

        }
    }

    public static class CustomCallable<V> implements Callable<V> {
        @Override
        public V call() throws Exception {
            return null;
        }
    }
}


