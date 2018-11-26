package org.didinem.sample.client.rpc;

import com.google.common.collect.Maps;

import java.util.Map;

/**
 * 每个Channel应该单独这个Map
 *
 * Created by didinem on 11/26/2018.
 */
public class ResponseFutureMap {

    private static Map<String, ResponseFuture> map = Maps.newConcurrentMap();

    public static void put(String key, ResponseFuture requestFuture) {
        map.put(key, requestFuture);
    }

    public static ResponseFuture get(String key) {
        return map.get(key);
    }

    public static void remove(String key) {
        map.remove(key);
    }

}
