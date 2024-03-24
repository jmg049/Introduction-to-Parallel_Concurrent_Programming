import java.util.concurrent.ConcurrentHashMap;

public class ConcurrentDataStructures {
    public static void main(String[] args) {
        // Create a ConcurrentHashMap
        ConcurrentHashMap<String, Integer> concurrentMap = new ConcurrentHashMap<>();

        // Create and start multiple threads
        Thread writerThread1 = new Thread(new Writer(concurrentMap, "Thread 1"));
        Thread writerThread2 = new Thread(new Writer(concurrentMap, "Thread 2"));
        Thread readerThread1 = new Thread(new Reader(concurrentMap, "Thread 3"));
        Thread readerThread2 = new Thread(new Reader(concurrentMap, "Thread 4"));

        writerThread1.start();
        writerThread2.start();
        readerThread1.start();
        readerThread2.start();
    }
}

// Writer class to perform write operations on the ConcurrentHashMap
class Writer implements Runnable {
    private ConcurrentHashMap<String, Integer> map;
    private String threadName;

    public Writer(ConcurrentHashMap<String, Integer> map, String threadName) {
        this.map = map;
        this.threadName = threadName;
    }

    @Override
    public void run() {
        for (int i = 1; i <= 5; i++) {
            // Perform write operation (put) on the map
            map.put("Key" + i, i);
            System.out.println(threadName + " wrote: Key" + i + " -> " + i);

            try {
                Thread.sleep(500); // Simulate some processing time
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}

// Reader class to perform read operations on the ConcurrentHashMap
class Reader implements Runnable {
    private ConcurrentHashMap<String, Integer> map;
    private String threadName;

    public Reader(ConcurrentHashMap<String, Integer> map, String threadName) {
        this.map = map;
        this.threadName = threadName;
    }

    @Override
    public void run() {
        for (int i = 1; i <= 5; i++) {
            // Perform read operation (get) on the map
            Integer value = map.get("Key" + i);
            System.out.println(threadName + " read: Key" + i + " -> " + value);

            try {
                Thread.sleep(1500); // Simulate some processing time
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
