import java.util.ArrayList;
import java.util.List;

public class WriteWrite {
    private static final int CAPACITY = 100;
    private static final List<Integer> DATA = new ArrayList<>(CAPACITY);


    private static void write() throws InterruptedException {
        for (int i = 0; i < DATA.size(); i++) {
            int x = DATA.get(i);
            int y = x * x;
            Thread.sleep(10);
            DATA.set(i, y);
        }
    }

    private static void writeTwo() throws InterruptedException {
        for (int i = 0; i < DATA.size(); i++) {
            int x = DATA.get(i);
            int y = -(x + x);
            Thread.sleep(10);
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
        final Thread threadTwo = new Thread(() -> {
            try {
                writeTwo();
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        });
        System.out.println(DATA);

        threadOne.start();
        threadTwo.start();

        threadOne.join();
        threadTwo.join();
        System.out.println(DATA);

    }
}
