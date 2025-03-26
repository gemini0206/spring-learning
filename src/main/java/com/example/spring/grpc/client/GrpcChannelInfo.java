package com.example.spring.grpc.client;

import lombok.Builder;
import lombok.Data;

@Data
@Builder(builderClassName = "Builder", toBuilder = true)
public class GrpcChannelInfo {

    private String channelName;
    private String host;
    private Integer port;

}
