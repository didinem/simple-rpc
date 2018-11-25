package org.didinem.sample.client.async.test;

import com.google.common.collect.Lists;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

/**
 * Created by didinem on 11/25/2018.
 */
public class Client {

    private static Runnable task = new Runnable() {
        @Override
        public void run() {
            System.out.println("客户端：发送消息给服务端");
            System.out.println("客户端：服务端正在执行任务，耗时约2s");
            try {
                TimeUnit.SECONDS.sleep(2);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            System.out.println("客户端：接收到服务端返回数据");
            // 通知消费者线程
            ResponseFuture<?> responseFuture = null;
            if (responses.size()>0) {
                responseFuture = responses.get(0);
            }

            if (responseFuture == null) {
                return;
            }

            responseFuture.receive("server response:bbb");
        }
    };

    private static List<ResponseFuture<?>> responses = Lists.newArrayList();

    private ExecutorService executorService = Executors.newFixedThreadPool(5);


    public ResponseFuture<String> sendRequest() {
        executorService.submit(task);
        ResponseFuture<String> responseFuture = new ResponseFuture<>();
        responses.add(responseFuture);
        return responseFuture;
    }

}
