package executor;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * 如果当有一个线程被提交执行的时候，就会先看corePool线程池的是不是已经满了，如果没有满的时候就会直接创建一个线程来执行任务
 * 如果corePool核心线程池已经满的时候，此时只能把被提交的线程放到LinkedBlockingQueue这个无界阻塞队列中等待
 * 如果corePool线程池中的线程要是有完成的任务之后，就会继续向LinkedBlockingQueue队列中去取排在前面的线程去执行任务，然后就会一直反复循环这个从第一步到第三步这个过程
 * 如果使用这个线程池会出现的一些缺点：
 * 当线程池中的线程数达到corePoolSize后，新任务将在无界队列中等待，因此线程池中的线程数不会超过corePoolSize。
 * 无界队列时maximumPoolSize将是一个无效参数
 * 使用无界队列时keepAliveTime将是一个无效参数
 * 由于使用无界队列，运行中的FixedThreadPool（未执行方法shutdown()或
 * shutdownNow()）不会拒绝任务（不会调用RejectedExecutionHandler.rejectedExecution方法）
 */
public class FixedThreadPoolDemo {

    public static void main(String[] args) throws InterruptedException {
        ExecutorService fixedThreadPool = Executors.newFixedThreadPool(3);

        for (int i = 0; i < 10; i++) {
            final int ind = i;
            fixedThreadPool.submit(new Runnable() {
                @Override
                public void run() {
                    try {
                        System.out.println(ind);
                        Thread.sleep(2000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            });
        }

        System.out.println(" start shutdown");

        // shutdown（）方法表明关闭已在Executor上调用，因此不会再向DelayedPool添加任何其他任务
        fixedThreadPool.shutdown();

        // 线程不执行完，不继续执行
        while (!fixedThreadPool.isTerminated()) {

        }
        System.out.println("end");
    }

}
