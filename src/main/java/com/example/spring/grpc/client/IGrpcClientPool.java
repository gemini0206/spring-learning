package com.example.spring.grpc.client;

import io.grpc.ManagedChannel;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;

import javax.annotation.concurrent.ThreadSafe;
import java.util.Set;

@ThreadSafe
public interface IGrpcClientPool {

    public ManagedChannel borrowChannel(String host, int port);

    public ManagedChannel borrowChannel(String shortcut) ;

    public void setShortcut(String key, String host, int port) throws NullPointerException;

    public Set<String> shortcuts();

    @Builder
    @EqualsAndHashCode
    @Getter
    public class Address {
        String host;
        int port;
    }
}
