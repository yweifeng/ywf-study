import java.util.concurrent.CountDownLatch;
import java.util.concurrent.Semaphore;

/**
 * SemaphoreEemo
 * Semaphore是一个计数信号量，常用于限制可以访问某些资源（物理或逻辑的）线程数目。
 */
public class SemaphoreEemo {

    public final static int THREAD_NUM = 30;

    public static void main(String[] args) throws InterruptedException {
        final Semaphore semaphore = new Semaphore(3);
        final CountDownLatch countDownLatch = new CountDownLatch(THREAD_NUM);
        // 30 个人 3个窗口办理服务

        for (int i = 1; i <= THREAD_NUM; i++) {
            final int num = i;
            new Thread(new Runnable() {
                public void run() {
                    try {
                        semaphore.acquire();
                        System.out.println("人员:" + num + "正在办理业务");
                        Thread.sleep(100);
                        System.out.println("人员:" + num + "办理完成");
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    } finally {
                        semaphore.release();
                        countDownLatch.countDown();
                    }
                }
            }).start();
        }

        countDownLatch.await();
        System.out.println("30个人全部办理完毕");
    }
}
