import java.util.ArrayList;
import java.util.List;

public class DataRace {


    private final static List<Integer> DATA = new ArrayList<>(1000);

    private static void update() throws InterruptedException {
        for (int i = 0; i < DATA.size(); i++) {
            int x = DATA.get(i);
            int y = x * x;
            Thread.sleep(10);
            DATA.set(i, y);
        }
    }

    private static void updateTwo() throws InterruptedException {
        for (int i = 0; i < DATA.size(); i++) {
            int x = DATA.get(i);
            int y = -(x + x);
            Thread.sleep(10);
            DATA.set(i, y);
        }
    }

    private static void print() {
        System.out.println(DATA);
    }

    public static void main(String[] args) throws InterruptedException {


        System.out.println("== Read/Read ==");
        System.out.println("---------------\n");

        System.out.println("== Read/Write ==");

        for (int i = 0; i < 100; i++) {
            DATA.add(i);
        }

        System.out.println(DATA + "\n");
        final Thread threadThree = new Thread(() -> {
            try {
                update();
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        });
        final Thread threadFour = new Thread(DataRace::print);

        threadThree.start();
        Thread.sleep(100);
        threadFour.start();

        threadThree.join();
        threadFour.join();
        System.out.println("---------------\n");

        System.out.println("== Write/Write ==");

        DATA.clear();
        for (int i = 0; i < 100; i++) {
            DATA.add(i);
        }
        System.out.println(DATA);
        final Thread threadFive = new Thread(() -> {
            try {
                update();
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        });

        final Thread threadSix = new Thread(() -> {
            try {
                updateTwo();
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        });

        threadFive.start();
        threadSix.start();

        threadFive.join();
        threadSix.join();
        System.out.println(DATA);
        System.out.println("---------------\n");

    }
}

