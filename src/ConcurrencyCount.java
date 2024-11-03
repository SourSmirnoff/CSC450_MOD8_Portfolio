public class ConcurrencyCount {

    private static final Object lock = new Object();
    private static boolean isCountingUpDone = false;

    public static void main(String[] args) {

        Thread thread1 = new Thread(new CountUp());
        Thread thread2 = new Thread(new CountDown());

        thread1.start();
        thread2.start();
    }

    static class CountUp implements Runnable {
        @Override
        public void run() {
            for (int i = 1; i <= 20; i++) {
                System.out.println("Counting Up: " + i);
                try {
                    Thread.sleep(100); // sSleep to make output readable
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            // signal counting up is done
            synchronized (lock) {
                isCountingUpDone = true;
                lock.notify();
            }
        }
    }

    static class CountDown implements Runnable {
        @Override
        public void run() {
            // wait til counting up is done
            synchronized (lock) {
                while (!isCountingUpDone) {
                    try {
                        lock.wait();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
            // Start counting down
            for (int i = 20; i >= 0; i--) {
                System.out.println("Counting Down: " + i);
                try {
                    Thread.sleep(100); // sleep to make output readable
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}