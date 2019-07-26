package executor;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * CachedThreadPool中线程实例默认超时时间为60s，超过这个时间，
 * 线程实例停止并被移出CachedThreadPool，适用于生存期短、异步的线程任务。
 * <p>
 * 从源码可以看出CachedThreadPool所使用的线程最大数量为Integer.MAX_VALUE，
 * 也就是说在执行过程中只要前面的任务没有完成，那么线程数量就不断增加，这样的话会产生大量线程使用过程可能存在栈溢出的问题
 */
public class CachedThreadPoolDemo {

    public static void main(String[] args) {
        ExecutorService cachedThreadPool = Executors.newCachedThreadPool();
        for (int i = 0; i < 5000; i++) {
            final int ind = i;
            cachedThreadPool.submit(new Runnable() {
                @Override
                public void run() {

                    System.out.println("ind=" + ind);
                    try {
                        Thread.sleep(100);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            });
        }

        System.out.println("start shutdown");
        cachedThreadPool.shutdown();
        while (!cachedThreadPool.isTerminated()) {

        }

        System.out.println("end");

    }
}
