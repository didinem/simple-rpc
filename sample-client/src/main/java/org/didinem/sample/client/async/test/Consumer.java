package org.didinem.sample.client.async.test;

/**
 * Created by didinem on 11/25/2018.
 */
public class Consumer {

    public static void main(String[] args) {
        Client client = new Client();

        System.out.println("消费者发送消息");
        ResponseFuture<String> responseFuture = client.sendRequest();
        String s = responseFuture.get();
        System.out.println(s);
    }

}
