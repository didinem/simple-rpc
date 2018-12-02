package org.didinem.sample.rpc;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.io.Serializable;

/**
 * Created by didinem on 11/11/2018.
 */
@Getter
@Setter
@ToString
public class RpcResponse implements Serializable {

    private String rpcID;

    private Object response;

}
