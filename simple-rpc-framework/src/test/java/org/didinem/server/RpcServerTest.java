package org.didinem.server;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

/**
 * Created by didinem on 4/3/2017.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("/spring-server-test.xml")
public class RpcServerTest {

    @Test
    public void test() {
        System.out.println("aaa");
    }

}
