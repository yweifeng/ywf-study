import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

/**
 * 阻塞队列 读写分离锁
 *
 * put/take方法 阻塞  put 队列满时候阻塞、take队列为空时候阻塞
 * offer/poll方法  --当队列已满或为空时，调用offer/poll方法为非阻塞的，通过返回值来告知调用方是否完成入队操作（true or false）
 *                或是否获取到队列中的元素（null or 非null）。这种方式相对而言比较优雅，所以在实际场景中比较常见。
 * add/remove方法
 *               --抛异常。这种调用方式，通常而言并不优雅，因此使用场景也比较少见。
 */
public class BlockQueueDemo {


    // 最多存储10个鸡蛋
    final static BlockingQueue<Integer> basketQueue = new LinkedBlockingQueue<Integer>(50);

    // 生产
    public void produce(String threadName) throws InterruptedException {
        // 超过100不再添加
        System.out.println(threadName + " 生产 一个鸡蛋,鸡蛋总量" + basketQueue.size());
        // 篮子满阻塞
        basketQueue.put(1);
    }

    // 消费
    public int consumer(String threadName) throws InterruptedException {
        // 队列空时阻塞
        System.out.println(threadName + " 消费 一个鸡蛋,鸡蛋总量" + basketQueue.size());
        return basketQueue.take();
    }

    public static void main(String[] args) throws InterruptedException {
        final BlockQueueDemo blockQueueDemo = new BlockQueueDemo();

        // 生产者
        new Thread(new Runnable() {
            public void run() {
                try {
                    for (int i = 0; i < 100; i++) {
                        blockQueueDemo.produce(Thread.currentThread().getName());
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }).start();

        // 消费者
        new Thread(new Runnable() {
            public void run() {
                try {
                    for (int i = 0; i < 100; i++) {
                        blockQueueDemo.consumer(Thread.currentThread().getName());
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }).start();

        System.out.println("end");
    }

}
