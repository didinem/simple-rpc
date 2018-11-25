package org.didinem.sample.server.rpc;

import lombok.Getter;
import lombok.Setter;

import java.net.InetAddress;
import java.net.UnknownHostException;

/**
 * Created by didinem on 11/25/2018.
 */
@Getter
@Setter
public class ServerConfig {

    private String hostAddress;

    private int port;

    public ServerConfig() {
        hostAddress = null;
        try {
            hostAddress = InetAddress.getLocalHost().getHostAddress();
        } catch (UnknownHostException e) {
            e.printStackTrace();
        }
        if (hostAddress == null) {
            hostAddress = "127.0.0.1";
        }
        port = 20880;
    }
}
