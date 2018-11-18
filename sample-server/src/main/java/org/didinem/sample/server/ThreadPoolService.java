package org.didinem.sample.server;

import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Created by didinem on 11/17/2018.
 */
public class ThreadPoolService {

    private AtomicInteger poolNumber = new AtomicInteger(1);

    private ExecutorService threadPool = Executors.newFixedThreadPool(10, new ThreadFactory() {
        @Override
        public Thread newThread(Runnable r) {
            Thread thread = new Thread(r);
            thread.setName("service-pool-" + poolNumber.getAndIncrement());
            return thread;
        }
    });

    public void submitTask(ServiceTask serviceTask) {
        threadPool.submit(serviceTask);
    }

    public void submitTask(Runnable task) {
        threadPool.submit(task);
    }

}
