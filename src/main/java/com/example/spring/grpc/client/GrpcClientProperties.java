package com.example.spring.grpc.client;

import com.google.common.collect.Lists;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.List;

@Data
@ConfigurationProperties(prefix = "grpc-client")
public class GrpcClientProperties {
    private Long futureTimeout = 500L;
    private Long stubDeadline = 500L;
    private Long shortStubDeadline = 500L;
    private List<GrpcChannelInfo> channelInfo = Lists.newArrayList();
}
