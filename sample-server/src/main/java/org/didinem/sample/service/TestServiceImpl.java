package org.didinem.sample.service;

/**
 * Created by didinem on 11/10/2018.
 */
public class TestServiceImpl implements TestService {

    @Override
    public String test(String input) {
        return "input is : " + input;
    }

}
