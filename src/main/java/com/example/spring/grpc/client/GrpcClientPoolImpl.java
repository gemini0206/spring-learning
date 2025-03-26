package com.example.spring.grpc.client;

import com.example.spring.grpc.server.GrpcServerProperties;
import com.google.common.base.Strings;
import com.google.common.base.Throwables;
import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.Set;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;

@Slf4j
@Component
public class GrpcClientPoolImpl implements IGrpcClientPool {
    private final Map<String, Address> shortcuts = new ConcurrentHashMap<>();
    private final Map<Address, BlockingQueue<ManagedChannel>> pooledChannelMap = new ConcurrentHashMap<>();

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
                result = borrowChannel(new Address.AddressBuilder().host(host).port(port).build());
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
            result = borrowChannel(shortcuts.get(shortcut));
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
    public Address addressOf(String shortcut) {
        return shortcuts.get(shortcut);
    }

    @Override
    public void clearPooledObject(String shortcut) {
        BlockingQueue<ManagedChannel> managedChannels = pooledChannelMap.get(shortcuts.get(shortcut));
        if (managedChannels != null && !managedChannels.isEmpty()) {
            Address address = addressOf(shortcut);
            log.info("[INFO] clear pooled object for shortcut {} on {}:{}", shortcut, address.getHost(), address.getPort());
            managedChannels.forEach(channel -> {
                log.info("[INFO] shutdown channel of {}:{}", address.host, address.port);
                terminateChannel(channel, address);
            });
            managedChannels.clear();
            pooledChannelMap.remove(shortcuts.get(shortcut));
        }
    }

    @Override
    public Set<String> shortcuts() {
        return shortcuts.keySet();
    }

    private ManagedChannel borrowChannel(Address address) {
        ManagedChannel result = null;
        if (address != null) {
            BlockingQueue<ManagedChannel> channels = pooledChannelMap.get(address);
            if (channels == null) {
                log.debug("[DEBUG] create the channel pool.");
                channels = pooledChannelMap.putIfAbsent(address, new LinkedBlockingQueue<>());
                channels = channels == null ? pooledChannelMap.get(address) : channels;
            }
            try {
                log.debug("[DEBUG] retrive a pooled channel of {}:{}", address.host, address.port);
                result = channels.poll();
                if (result == null) {
                    log.warn("polled all broken channel for {}:{}, start create new",
                            address.host, address.port);
                    result = createChannel(address.host, address.port);
                }
                channels.put(result);
            } catch (Exception e) {
                log.error("[ERROR] create and pool channel occured error. {}",
                        Throwables.getStackTraceAsString(e));
            }
        }
        return result;
    }

    private ManagedChannel createChannel(String host, int port) {
        int clientWorkerCount = grpcServerProperties == null ? 10 : grpcServerProperties.getClientWorkerCount();
        log.info("[DEBUG]create channel for {}:{}'s client worker count is {}", host, port, clientWorkerCount);
        ManagedChannelBuilder<?> builder = ManagedChannelBuilder.forAddress(host, port);
        return builder.usePlaintext().maxInboundMessageSize(8 * 1024 * 1024).build();
    }

    private void terminateChannel(ManagedChannel channel, Address address) {
        if (channel != null) {
            try {
                log.debug("[DEBUG] destroying a channel of {} ", channel);
                if (!channel.shutdown().awaitTermination(3000, TimeUnit.MILLISECONDS)) {
                    channel.shutdownNow().awaitTermination(3000, TimeUnit.MILLISECONDS);
                    if (address == null) {
                        log.warn("Force shutdown UNPOOLED channel: {}, result is {}.", channel, channel.isTerminated());
                    } else {
                        log.warn("Force shutdown POOLED channel: {}:{}, result is {}.", address.host, address.port, channel.isTerminated());
                    }
                }
            } catch (Exception e) {
                log.warn("Destroy channel occured error ", e);
            }
        }

    }
}
