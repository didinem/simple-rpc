package org.didinem.sample.server.rpc;

import org.didinem.sample.server.RpcSampleServer;

import java.net.InetAddress;
import java.net.UnknownHostException;

/**
 * Created by didinem on 11/18/2018.
 */
public class ProviderRegisterService {

    private ZKRegisterService zkRegisterService;

    public void registe(ProviderInfo providerInfo) {
        // 向注册中心注册服务
        zkRegisterService.register(providerInfo);

        // 在本机端口监听请求
        String hostAddress = null;
        try {
            hostAddress = InetAddress.getLocalHost().getHostAddress();
        } catch (UnknownHostException e) {
            e.printStackTrace();
        }

        //TODO 配置化,只初始化一个Server
        int port = 20880;
        RpcSampleServer rpcSampleServer = new RpcSampleServer(hostAddress, port);
        try {
            rpcSampleServer.bind();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


}
