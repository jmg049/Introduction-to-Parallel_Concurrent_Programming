import java.util.ArrayList;
import java.util.List;

public class ReadRead {
    private static final int CAPACITY = 1000;
    private static final List<Integer> DATA = new ArrayList<>(CAPACITY);

    private static void readAscending() {

        System.out.print("[");
        for (int i = 0; i < CAPACITY - 1; i++) {
            System.out.print(DATA.get(i) + ", ");
        }
        System.out.println(DATA.getLast() + "]");
    }

    private static void readDescending() {
        System.out.print("[");
        for (int i = 0; i < CAPACITY - 1; i++) {
            System.out.print(DATA.get(i) + ", ");
        }
        System.out.println(DATA.getLast() + "]");
    }

    public static void main(final String[] args) throws InterruptedException {
        for (int i = 0; i < CAPACITY; i++) {
            DATA.add(i + 1);
        }

        final Thread threadOne = new Thread(ReadRead::readAscending);
        final Thread threadTwo = new Thread(ReadRead::readDescending);

        threadOne.start();
        threadTwo.start();

        threadOne.join();
        threadTwo.join();

    }
}
