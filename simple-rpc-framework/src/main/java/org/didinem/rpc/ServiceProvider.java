package org.didinem.rpc;

/**
 * Created by didinem on 4/2/2017.
 */
public class ServiceProvider {

    private String ip;

    private String port;

    private Class interfaceClass;

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public String getPort() {
        return port;
    }

    public void setPort(String port) {
        this.port = port;
    }

    public Class getInterfaceClass() {
        return interfaceClass;
    }

    public void setInterfaceClass(Class interfaceClass) {
        this.interfaceClass = interfaceClass;
    }
}
