package org.didinem.sample.server.rpc;

import com.google.common.collect.Maps;
import org.didinem.sample.rpc.ProviderInfo;
import org.didinem.sample.server.RpcSampleServer;
import org.springframework.stereotype.Service;

import java.util.Map;

/**
 * Created by didinem on 11/25/2018.
 */
@Service
public class RpcServerService {

    private static final Map<ServerKey, RpcSampleServer> map = Maps.newHashMap();

    public void initServer(ProviderInfo providerInfo) {
        ServerKey serverKey = new ServerKey(providerInfo.getHostAddress(), providerInfo.getPort());
        if (map.containsKey(serverKey)) {
            return;
        }

        //TODO 配置化,只初始化一个Server
        RpcSampleServer rpcSampleServer = new RpcSampleServer(providerInfo.getHostAddress(), providerInfo.getPort());
        try {
            rpcSampleServer.bind();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static class ServerKey {
        private String hostAddress;
        private int port;

        public ServerKey(String hostAddress, int port) {
            this.hostAddress = hostAddress;
            this.port = port;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;

            ServerKey serverKey = (ServerKey) o;

            if (port != serverKey.port) return false;
            return hostAddress.equals(serverKey.hostAddress);
        }

        @Override
        public int hashCode() {
            int result = hostAddress.hashCode();
            result = 31 * result + port;
            return result;
        }
    }

}
