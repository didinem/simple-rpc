package org.didinem.sample.client.async.test;

/**
 * Created by didinem on 11/25/2018.
 */
public class ResponseFuture<T> {

    /**
     * 服务端服务返回后，客户端对消费者线程的通知
     */
    private Object notification = new Object();

    /**
     * 服务端是否返回，默认未返回
     */
    private boolean isReturn = false;

    /**
     * 服务端返回数据
     */
    private T response;

    public void receive(Object response) {
        this.response = (T) response;
        isReturn = true;
        System.out.println("客户端通知消费者");
        synchronized (notification) {
            notification.notify();
        }
    }

    public T get() {
        synchronized (notification) {
            while (!isReturn) {
                try {
                    notification.wait();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
        return response;
    }


}
