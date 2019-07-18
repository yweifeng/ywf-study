import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

/**
 * 非阻塞队列
 * 是Queue的一个安全实现．Queue中元素按FIFO原则进行排序．采用CAS操作，来保证元素的一致性。
 * add      增加一个元索               如果队列已满，则抛出一个IIIegaISlabEepeplian异常
 * remove   移除并返回队列头部的元素     如果队列为空，则抛出一个NoSuchElementException异常
 * element  返回队列头部的元素          如果队列为空，则抛出一个NoSuchElementException异常
 * offer    添加一个元素并返回true      如果队列已满，则返回false
 * poll     移除并返问队列头部的元素     如果队列为空，则返回null
 * peek     返回队列头部的元素          如果队列为空，则返回null
 * put      添加一个元素                如果队列满，则阻塞
 * take     移除并返回队列头部的元素     如果队列为空，则阻塞
 */
public class ConcurrentLinkedQueueDemo {

    public static ConcurrentLinkedQueue basketQueue = new ConcurrentLinkedQueue();

    // 生产
    public void produce(String threadName) throws InterruptedException {
        System.out.println(threadName + " 生产 一个鸡蛋,鸡蛋总量" + basketQueue.size());
        basketQueue.add(1);
    }

    // 消费
    public void consumer(String threadName) throws InterruptedException {
        System.out.println(threadName + " 消费 一个鸡蛋,鸡蛋总量" + basketQueue.size());
        basketQueue.poll();
    }

    public static void main(String[] args) throws InterruptedException {
        final CountDownLatch countDownLatch = new CountDownLatch(2);
        final ConcurrentLinkedQueueDemo queueDemo = new ConcurrentLinkedQueueDemo();

        // 生产者
        new Thread(new Runnable() {
            public void run() {
                try {
                    for (int i = 0; i < 100; i++) {
                        queueDemo.produce(Thread.currentThread().getName());
                    }
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } finally {
                    countDownLatch.countDown();
                }
            }
        }).start();

        // 消费者
        new Thread(new Runnable() {
            public void run() {
                try {
                    for (int i = 0; i < 100; i++) {
                        queueDemo.consumer(Thread.currentThread().getName());
                    }
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } finally {
                    countDownLatch.countDown();
                }
            }
        }).start();

        countDownLatch.await();
        System.out.println("end");
    }
}
