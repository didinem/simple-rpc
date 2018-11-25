package org.didinem.sample.server.rpc;

import com.google.common.collect.Maps;

import java.util.Map;

/**
 * Created by didinem on 11/25/2018.
 */
public class ProviderBeanMapper {

    private static final Map<String, Object> mapper = Maps.newHashMap();

    public static void addMapping(String qualifiedClassName, Object providerObject) {
        mapper.put(qualifiedClassName, providerObject);
    }

    public static Object getMapping(String qualifiedClassName) {
        return mapper.get(qualifiedClassName);
    }

}
