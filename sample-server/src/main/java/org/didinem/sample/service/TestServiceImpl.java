package org.didinem.sample.service;

import org.springframework.stereotype.Service;

/**
 * Created by didinem on 11/10/2018.
 */
@Service
public class TestServiceImpl implements TestService {

    @Override
    public String test(String input) {
        return "input is : " + input;
    }

}
