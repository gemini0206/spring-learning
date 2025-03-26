package com.example.spring.grpc.client;

import com.example.spring.grpc.HelloServiceGrpc;
import io.grpc.Channel;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class GrpcClientConfig {
    static final String SERVER_CHANNEL_NAME = "server";

    @Resource
    GrpcClientProperties grpcClientProperties;

    Long stubDeadline;

    Long shortStubDeadline;

    @Resource
    IGrpcClientPool pool;

    @PostConstruct
    public void init() throws Exception {
        stubDeadline = grpcClientProperties.getStubDeadline();
        shortStubDeadline = grpcClientProperties.getShortStubDeadline();
        List<GrpcChannelInfo> channelInfoList = grpcClientProperties.getChannelInfo();
        for (GrpcChannelInfo channelInfo : channelInfoList) {
            pool.setShortcut(channelInfo.getChannelName(), channelInfo.getHost(), channelInfo.getPort());
        }
    }

    public HelloServiceGrpc.HelloServiceBlockingStub helloServiceBlockingStub() {
        Channel channel = pool.borrowChannel(SERVER_CHANNEL_NAME);
        return HelloServiceGrpc.newBlockingStub(channel).withDeadlineAfter(stubDeadline, TimeUnit.MILLISECONDS);
    }
}
