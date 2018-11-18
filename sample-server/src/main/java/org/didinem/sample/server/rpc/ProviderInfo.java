package org.didinem.sample.server.rpc;

import lombok.Getter;
import lombok.Setter;

/**
 * Created by didinem on 11/18/2018.
 */
@Setter
@Getter
public class ProviderInfo {

    private Class providerInterfaceClass;

    private String hostAddress;

    private int port;


    public String getQualifiedClassName() {
        return providerInterfaceClass.getName();
    }

}
