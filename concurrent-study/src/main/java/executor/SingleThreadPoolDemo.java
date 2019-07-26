package executor;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * 也就是主线程如果有提交一个新的线程过来的话，他会先去看看SingleThreadExecutor线程池是不是有一个线程正再执行，如果没有一个线程的话，就会去直接执行刚刚提交过来的这个线程
 * 如果已经有一个线程正在执行的话，就会吧这个线程放到无节限的对列中去执行
 * 当corePool这个核心线程池中的线程执行完毕之后，他就会去从哪个LinkedBlockingQueue无界限对列中去取到一个线程继续来执行，然后依次反复执行
 */
public class SingleThreadPoolDemo {

    public static void main(String[] args) {
        ExecutorService executorService = Executors.newSingleThreadExecutor();

        for (int i = 0; i < 10; i++) {
            final int ind = i;
            executorService.submit(new Runnable() {
                @Override
                public void run() {
                     System.out.println(ind);
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            });
        }

        System.out.println("start shutdown");
        executorService.shutdown();
        while (!executorService.isTerminated()) {

        }
        System.out.println("end");
    }
}
