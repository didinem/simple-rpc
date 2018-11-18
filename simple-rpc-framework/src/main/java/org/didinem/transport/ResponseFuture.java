package org.didinem.transport;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * Created by didinem on 5/7/2017.
 */
public class ResponseFuture<T> {

    private T response;

    private Lock lock = new ReentrantLock();

    private Condition condition = lock.newCondition();

    public T get(long time) {
        lock.lock();
        try {
            if (response == null) {
                condition.await(time, TimeUnit.MILLISECONDS);
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            lock.unlock();
        }

        return response;
    }

    public T get() {
        return get(1000L);
    }

    public void set(T t) {
        lock.lock();
        try {
            this.response = t;
            condition.signal();
        } finally {
            lock.unlock();
        }
    }


}
