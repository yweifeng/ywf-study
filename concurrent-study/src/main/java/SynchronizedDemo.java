import java.util.Vector;

/**
 * synchronized（悲观锁）：
 *  优点：
 *      1、保证了多线程之间运行具有原子性、可见性和有序性
 *      2、java中每个对象都可以作为锁(JMM堆对象头)、
 *         动态方法加锁：加锁对象为当前实例对象
 *         静态方法加锁：加锁对象为当前类的class对象
 *         方法块加锁：加锁对象为括号里的对象
 *
 *  缺点：
 *      1、多线程之间会竞争锁、导致线程堵塞
 *
 *  底层原理和实现机制：
 *      1、访问同步代码块：生成2个汇编指令——monitorenter和monitorexit
 *      2、同步方法：生成2个汇编指令——revokevirtual和areturn
 *
 *  java虚拟机对synchronized进行优化（java6版本之后）
 *      随着锁的竞争，锁会进行升级，升级过程是单向的
 *      无锁->偏向锁->轻量级锁->重量级锁
 */
public class SynchronizedDemo {

    private static int num = 0;

    public void incre() {
        try {
            Thread.sleep(100);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        synchronized (this) {
            num++;
        }
    }

    public static void main(String[] args) throws InterruptedException {
        final SynchronizedDemo synchronizedDemo = new SynchronizedDemo();
        Vector<Thread> vector = new Vector<Thread>();
        for (int i = 0; i < 1000; i++) {
            Thread t = new Thread(new Runnable() {
                public void run() {
                    synchronizedDemo.incre();
                }
            });
            vector.add(t);
            t.start();
        }

        // 等待线程执行完成
        for (Thread thread: vector) {
            thread.join();
        }
        System.out.println("num=" + num);
    }
}
