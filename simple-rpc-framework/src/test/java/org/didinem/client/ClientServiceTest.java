package org.didinem.client;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

/**
 * Created by didinem on 4/3/2017.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("/spring-client-test.xml")
public class ClientServiceTest {

    @Autowired
    private ClientService clientService;

    @Test
    public void test() {
        clientService.client();
    }

}
