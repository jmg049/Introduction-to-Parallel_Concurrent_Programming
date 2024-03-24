import java.util.ArrayList;
import java.util.List;

public class ReadWrite {
    private static final int CAPACITY = 1000;
    private static final List<Integer> DATA = new ArrayList<>(CAPACITY);

    private static void read() {
        System.out.println(DATA);
    }

    private static void write() throws InterruptedException {
        for (int i = 0; i < DATA.size(); i++) {
            int x = DATA.get(i);
            int y = x * x;
            Thread.sleep(1);
            DATA.set(i, y);
        }
    }


    public static void main(final String[] args) throws InterruptedException {
        for (int i = 0; i < CAPACITY; i++) {
            DATA.add(i + 1);
        }

        final Thread threadOne = new Thread(() -> {
            try {
                write();
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        });
        final Thread threadTwo = new Thread(ReadWrite::read);

        threadOne.start();
        Thread.sleep(100);
        threadTwo.start();

        threadOne.join();
        threadTwo.join();

    }
}
