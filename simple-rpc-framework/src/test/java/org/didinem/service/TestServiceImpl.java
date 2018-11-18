package org.didinem.service;

import org.didinem.rpc.annotation.RpcService;
import org.springframework.stereotype.Component;

/**
 * Created by didinem on 4/3/2017.
 */
@Component
@RpcService("org.didinem.service.TestService")
public class TestServiceImpl implements TestService {
    public String test(String name) {
        return null;
    }
}
