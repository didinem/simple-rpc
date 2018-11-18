package org.didinem.client;

import org.didinem.service.TestService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * Created by didinem on 4/3/2017.
 */
@Component
public class ClientService {

    @Autowired
    private TestService testService;

    public void client() {
        String result = testService.test("aa");
        System.out.println(result);
    }

}
