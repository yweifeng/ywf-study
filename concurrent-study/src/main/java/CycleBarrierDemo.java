import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.CyclicBarrier;

/**
 * CycleBarrier 环形栅栏
 * <p>
 * 通过它可以实现让一组线程等待至某个状态之后再全部同时执行。
 * 叫做回环是因为当所有等待线程都被释放以后，CyclicBarrier可以被重用。
 * 我们暂且把这个状态就叫做barrier，当调用await()方法之后，线程就处于barrier了。
 */
public class CycleBarrierDemo {
    public static final int THREAD_NUM = 50;

    public static void main(String[] args) throws InterruptedException {
        // 60个人坐车，20个人一车，人满发车

        final CyclicBarrier cyclicBarrier = new CyclicBarrier(20);
        final CountDownLatch countDownLatch = new CountDownLatch(THREAD_NUM);

        for (int i = 1; i <= THREAD_NUM; i++) {
            final int num = i;
            new Thread(new Runnable() {
                public void run() {
                    try {
                        System.out.println("人员:" + num + "开始上车");
                        Thread.sleep(5000);
                        // 等待人装满发车
                        cyclicBarrier.await();
                        System.out.println("人满发车");
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    } catch (BrokenBarrierException e) {
                        e.printStackTrace();
                    } finally {
                        countDownLatch.countDown();
                    }
                }
            }).start();
        }

        // 注意是await而不是wait
        countDownLatch.await();
        System.out.println("发车完成");
    }
}
