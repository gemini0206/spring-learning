package com.example.spring.grpc.server;

import javax.annotation.PostConstruct;
import java.util.List;

public class GrpcClientConfig {
    static final String SERVER_CHANNEL_NAME = "server";

    @PostConstruct
    public void init() throws Exception {
//        stubDeadline = brokerServerProperties.getGrpcClient().getStubDeadline();
//        shortStubDeadline = brokerServerProperties.getGrpcClient().getShortStubDeadline();
//        List<GrpcChannelInfo> channelInfoList = brokerServerProperties.getGrpcClient().getChannelInfo();
//        for (GrpcChannelInfo channelInfo : channelInfoList) {
//            pool.setShortcut(channelInfo.getChannelName(), channelInfo.getHost(), channelInfo.getPort());
//        }
    }
}
