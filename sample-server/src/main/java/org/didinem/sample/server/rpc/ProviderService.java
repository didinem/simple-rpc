package org.didinem.sample.server.rpc;

import org.didinem.sample.rpc.ProviderInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Created by didinem on 11/25/2018.
 */
@Service
public class ProviderService {

    @Autowired
    private ProviderRegisterService providerRegisterService;

    @Autowired
    private RpcServerService rpcServerService;

    public Object initProvider(Object providerObject) throws Exception {
        Class<?> providerClass = providerObject.getClass();
        Class<?>[] interfaces = providerClass.getInterfaces();

        if (interfaces.length != 1) {
            throw new Exception("invalid provider bean");
        }
        Class<?> qualifiedClassName = interfaces[0];

        ServerConfig serverConfig = new ServerConfig();
        ProviderInfo providerInfo = new ProviderInfo(qualifiedClassName, serverConfig.getHostAddress(), serverConfig.getPort());
        providerRegisterService.registe(providerInfo);
        rpcServerService.initServer(providerInfo);

        ProviderBeanMapper.addMapping(providerInfo.getQualifiedClassName(), providerObject);
        return providerObject;
    }

}
