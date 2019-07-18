/**
 * volatile 保证了共享内存的可见性、有序性、但不保证原子性
 * 使用volatile 一定要先了解java内存模型 JMM 和 MESI 缓存一致性协议
 *
 * 适用场景:
 *    1、对变量的写操作不依赖于当前值
 *    2、该变量没有包含在其他变量的不等式中
 *
 *    最适合一个线程写、多个线程读
 *
 * 于synchronized 相比：
 *    优点：synchronized是重量级锁，会导致线程多线程之前竞争堵塞。volatile不会线程阻塞
 *    缺点：不具备原子性
 *    volatile修饰共享变量、synchronized修饰对象、方法、代码块
 *    目的不同：volatile注重解决多线程之间共享变量的可见性和有序性
 *             synchronized注重解决多线程之间访问资源的同步性
 *
 * 底层原理和实现机制：
 *    使用volitale后，汇编代码会添加一行lock前缀指令
 *    lock前缀指令相当于一个内存屏障：
 *    1、解决指令重排序问题：在lock之前的指令不会在lock之后执行,在lock之后的指令也不会跑到lock之前执行。
 *    2、将修改的指实时刷新到主内存
 *    3、通过mesi协议、会导致各线程的共享变量失效、如果要操作、需从主内存获取最新共享变量到本地共享变量副本
 *
 */
public class VolatileDemo {

    public static volatile boolean initFlag = false;

    public static void main(String[] args) {

        // 1个写操作线程
        new Thread(new Runnable() {
            public void run() {
                VolatileDemo.initFlag = true;
            }
        }).start();

        // 多个读操作线程
        for (int i = 0; i < 1000 ; i++) {
            new Thread(new Runnable() {
                public void run() {
                    System.out.println(VolatileDemo.initFlag);
                }
            }).start();
        }

        if (Thread.activeCount() > 1) {
            Thread.yield();
        }

        System.out.println(initFlag);
    }
}
