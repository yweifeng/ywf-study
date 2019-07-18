import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

/**
 *  CountDownLatchDemo 计数器
 *  一个可以用来协调多个线程之间的同步，或者说起到线程之间的通信作用的工具类。
 *
 *  优点：
 *      某一线程在开始运行前等待n个线程执行完毕
 *      实现多个线程开始执行任务的最大并行性
 *
 *  缺点：
 *      CountDownLatch是一次性的，计数器的值只能在构造方法中初始化一次，
 *      之后没有任何机制再次对其设置值，当CountDownLatch使用完毕后，它不能再次被使用。
 *
 *  适用场景：
 *      1、某一线程在开始运行前等待n个线程执行完毕。
 *      将CountDownLatch的计数器初始化为n：new CountDownLatchDemo(n) ，每当一个任务线程执行完毕，就将计数器减1，
 *      countdownlatch.countDown()，当计数器的值变为0时，在CountDownLatch上 await() 的线程就会被唤醒。
 *      一个典型应用场景就是启动一个服务时，主线程需要等待多个组件加载完毕，之后再继续执行。
 *
 *      2、实现多个线程开始执行任务的最大并行性。
 *      注意是并行性，不是并发，强调的是多个线程在某一时刻同时开始执行。
 *      做法是初始化一个共享的CountDownLatch(1)，将其计数器初始化为1，多个线程在开始执行任务前
 *      首先 coundownlatch.await()，当主线程调用 countDown() 时，计数器变为0，多个线程同时被唤醒。
 */
public class CountDownLatchDemo {

    public final static int THREAD_NUM = 50;

    public static int num = 0;

    public void incre() {
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        synchronized (this) {
            num++;
        }
    }

    public static void main(String[] args) throws InterruptedException {
        final CountDownLatchDemo countDownLatchDemo = new CountDownLatchDemo();

        final CountDownLatch countDownLatch = new CountDownLatch(THREAD_NUM);

        for (int i = 0; i < THREAD_NUM; i++) {

            new Thread(new Runnable() {
                public void run() {
                    try {
                        countDownLatchDemo.incre();
                    } finally {
                        countDownLatch.countDown();
                    }
                }
            }).start();
        }

        // 设置超时时间
        // 注意是await而不是wait
        countDownLatch.await(10, TimeUnit.SECONDS);

        System.out.println(num);
    }
}
