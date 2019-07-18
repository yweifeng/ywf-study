import java.util.Vector;
import java.util.concurrent.atomic.AtomicInteger;

/**
 *  CAS:比较和交换 Conmpare And Swap
 *  用于实现多线程同步的原子指令。 它将内存位置的内容与给定值进行比较，只有在相同的情况下，将该内存位置的内容修改为新的给定值。
 *
 *  底层是CPU的一条指令,java中使用Unsafe方法调用native
 *  CAS有三个操作数,内存V,旧的预期数值A,要修改的更新值B.
 *  当且仅当预期值A和内存值V相同时,将内存值V修改为B。无论失败成功，操作之前都可以获取V中最新值
 *
 *  参考文章:https://www.jianshu.com/p/ae25eb3cfb5d
 *  优点：
 *      1、减少锁的开销、线程之前的阻塞和唤醒
 *  缺点：
 *      1、CPU开销大
 *      2、不能保证代码原子性
 *      3、ABA问题：
 *       线程1->A (发现还是A)
 *       线程2->A (A->执行逻辑->B,B->执行逻辑->A)
 *       解决方法：添加版本号
 *       A->B->A 变为 1A->2B->3A
 */
public class CasDemo {

    public static AtomicInteger count = new AtomicInteger(0);

    public static void main(String[] args) throws InterruptedException {
        Vector<Thread> vector = new Vector<Thread>();
        for (int i = 0; i < 1000; i++) {
            Thread t = new Thread(new Runnable() {
                public void run() {
                    count.incrementAndGet();
                }
            });
            vector.add(t);
            t.start();
        }

        // 等待线程执行完成
        for (Thread thread: vector) {
            thread.join();
        }
        System.out.println("num=" + count);
    }
}
