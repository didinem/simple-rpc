package org.didinem.sample.server.rpc;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.stereotype.Component;

/**
 * Created by didinem on 11/25/2018.
 */
@Component
public class RpcServiceBeanPostProcessor implements BeanPostProcessor {

    @Autowired
    private ProviderService providerService;

    @Override
    public Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {
        Object object = null;
        try {
            object = providerService.initProvider(bean);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return object;
    }
}
