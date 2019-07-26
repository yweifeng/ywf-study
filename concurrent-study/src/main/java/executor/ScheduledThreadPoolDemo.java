package executor;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * 首先主线程会先提交过来一个定时调度任务提交到DelayQueue这个阻塞队列中，
 * 如果ScheduledThreadPoolExecutor线程池中有空闲的线程就会去执行个ScheduledFutureTask里面的到期任务
 * 在执行完事之后，线程1修改ScheduledFutureTask的time变量为下次将要被执行的时间
 * 线程1把这个修改time之后的ScheduledFutureTask放回DelayQueue中（DelayQueue.add()）。
 *
 * scheduleAtFixedRate方法
 * 该方法设置了执行周期，下一次执行时间相当于是上一次的执行时间加上period，它是采用已固定的频率来执行任务：
 *
 * scheduleWithFixedDelay方法
 * 该方法设置了执行周期，与scheduleAtFixedRate方法不同的是，下一次执行时间是上一次任务执行完的系统时间加上period，
 * 因而具体执行时间不是固定的，但周期是固定的，是采用相对固定的延迟来执行任务：
 */
public class ScheduledThreadPoolDemo {

    public static void main(String[] args) {
        ScheduledExecutorService scheduledThreadPool = Executors.newScheduledThreadPool(3);

        for (int i = 0; i < 10; i++) {
            final int ind = i;
            scheduledThreadPool.scheduleWithFixedDelay(new Runnable() {
                @Override
                public void run() {
                    System.out.println(ind);
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            },0, 5, TimeUnit.SECONDS);

        }
        System.out.println("end");
    }
}
