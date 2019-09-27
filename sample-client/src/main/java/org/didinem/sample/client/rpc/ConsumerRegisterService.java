package org.didinem.sample.client.rpc;

import com.google.common.collect.Lists;
import lombok.Setter;
import org.didinem.sample.rpc.ProviderInfo;
import org.didinem.sample.rpc.ZKRegisterService;

import java.util.List;

@Setter
public class ConsumerRegisterService {

    private ZKRegisterService zkRegisterService;

    public List<ProviderInfo> subscribeAndList(Class providerInterfaceClass) {
        List<ProviderInfo> providerInfos = zkRegisterService.listProviderInfo(providerInterfaceClass);
        if (providerInfos == null || providerInfos.size() == 0) {
            return Lists.newArrayListWithCapacity(0);
        }

        return providerInfos;
    }

}
