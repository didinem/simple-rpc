package org.didinem.sample.server.rpc;

import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * Created by didinem on 11/18/2018.
 */
@Component
@Setter
public class ProviderRegisterService {

    @Autowired
    private ZKRegisterService zkRegisterService;

    public void registe(ProviderInfo providerInfo) {
        // 向注册中心注册服务
        zkRegisterService.register(providerInfo);
    }


}
