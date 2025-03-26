package com.example.spring.grpc.client;

import com.example.spring.grpc.server.GrpcServerProperties;
import com.google.common.base.Strings;
import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.Set;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ConcurrentHashMap;

@Slf4j
@Component
public class GrpcClientPoolImpl implements IGrpcClientPool {
    private Map<String, Address> shortcuts = new ConcurrentHashMap<>();
    private Map<Address, BlockingQueue<ManagedChannel>> pooledChannelMap;

    private final GrpcServerProperties grpcServerProperties;

    @Autowired
    public GrpcClientPoolImpl(GrpcServerProperties grpcServerProperties) {
        this.grpcServerProperties = grpcServerProperties;
    }

    @Override
    public ManagedChannel borrowChannel(String host, int port) {
        ManagedChannel result = null;
        if (!Strings.isNullOrEmpty(host)) {
            try {
                result = createChannel(host, port);
            } catch (Exception e) {
                log.error("BorrowOrRobChannel occured error: ", e);
            }
        }
        return result;
    }

    @Override
    public ManagedChannel borrowChannel(String shortcut) {
        ManagedChannel result = null;
        try {
            result = createChannel(shortcuts.get(shortcut).host, shortcuts.get(shortcut).port);
        } catch (Exception e) {
            log.error("BorrowOrRobChannel occured error: ", e);
        }
        return result;
    }

    @Override
    public void setShortcut(String key, String host, int port) throws NullPointerException {
        if (Strings.isNullOrEmpty(key) || Strings.isNullOrEmpty(host)) {
            throw new NullPointerException("You key or host should not been null.");
        }
        shortcuts.put(key, new Address.AddressBuilder().host(host).port(port).build());
    }

    @Override
    public Set<String> shortcuts() {
        return Set.of();
    }


    private ManagedChannel createChannel(String host, int port) throws Exception {
        int clientWorkerCount = grpcServerProperties == null ? 10 : grpcServerProperties.getClientWorkerCount();
        log.info("[DEBUG]create channel for {}:{}'s client worker count is {}", host, port, clientWorkerCount);
        ManagedChannelBuilder<?> builder = ManagedChannelBuilder.forAddress(host, port);
        return builder.usePlaintext().maxInboundMessageSize(8 * 1024 * 1024).build();
    }
}
