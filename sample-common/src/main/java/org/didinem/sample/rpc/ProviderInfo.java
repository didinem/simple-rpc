package org.didinem.sample.rpc;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

/**
 * Created by didinem on 11/18/2018.
 */
@Setter
@Getter
@AllArgsConstructor
public class ProviderInfo {

    private Class providerInterfaceClass;

    private String hostAddress;

    private int port;


    public String getQualifiedClassName() {
        return providerInterfaceClass.getName();
    }

}
