package org.didinem.sample.client.rpc;

import io.netty.channel.ChannelFuture;
import lombok.Getter;
import lombok.Setter;

/**
 * Created by didinem on 11/26/2018.
 */
@Setter
@Getter
public class ResponseFuture {

    private String rpcID;

    private ChannelFuture writeFuture;

    private volatile boolean writeSuccess = false;

    private volatile boolean isReturn = false;

    private Object receiveNotification = new Object();

    private Object response;

    public void receive(Object response) {
        this.response = response;
        isReturn = true;
        synchronized (receiveNotification) {
            receiveNotification.notify();
        }
    }

    public Object get() {
        synchronized (receiveNotification) {
            while (!isReturn) {
                try {
                    receiveNotification.wait();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
        return response;
    }


}
