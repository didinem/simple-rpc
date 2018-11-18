package org.didinem.sample;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.io.Serializable;

/**
 * Created by didinem on 11/10/2018.
 */
@Setter
@Getter
@ToString
public class RpcInvocation implements Serializable {

    private String interfaceQualifiedName;

    private String methodName;

    private Class[] parametersTypes;

    private Object[] parameters;

}
