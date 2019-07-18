import org.omg.Messaging.SYNC_WITH_TRANSPORT;

import java.util.Vector;
import java.util.concurrent.locks.ReentrantLock;

/**
 *  ReentrantLock 可重入锁
 *
 *  优点：
 *      1、等待可中断。
 *      2、公平锁。
 *      3、锁绑定多个条件.一个ReentrantLock对象可以同时绑定对个对象。
 *         ReenTrantLock提供了一个Condition（条件）类，用来实现分组唤醒需要唤醒的线程们
 *  缺点：
 *      1、发生异常，会造成死锁。解决方法：finally中释放锁
 *      2、只能在代码块中使用
 *
 *  底层原理：
 *
 *  适合场景：
 *      由于synchonized在1.5版本之后优化了锁升级、大部分情况下和lock的性能差不多。
 *      当需要使用ReentrantLock的三个有点的时候，选择该类型锁。
 */
public class ReentrantLockDemo {

    public static int num = 0;
    public static ReentrantLock lock = new ReentrantLock();

    public void incre() {
        try {
            lock.lock();
            num++;

            // 获取排队数量
            System.out.println(lock.getQueueLength());
        } finally {
            lock.unlock();
        }

    }

    public static void main(String[] args) {
        final ReentrantLockDemo lockDemo = new ReentrantLockDemo();
        for (int i = 0; i < 100; i++) {
            new Thread(new Runnable() {
                public void run() {
                    lockDemo.incre();
                }
            }).start();
        }

        while (Thread.activeCount()> 1) {
            Thread.yield();
        }

        System.out.println(num);
    }
}
