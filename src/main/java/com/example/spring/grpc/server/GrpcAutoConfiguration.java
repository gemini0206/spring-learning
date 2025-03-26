package com.example.spring.grpc.server;

import io.grpc.ServerBuilder;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import io.grpc.services.HealthStatusManager;

@Configuration
public class GrpcAutoConfiguration implements ApplicationContextAware {
    private ApplicationContext applicationContext;

    @Autowired
    private GrpcServerProperties grpcServerProperties;

    @Bean
	public Runner grpcServerRunner(HealthStatusManager
			healthStatusManager) {
		ServerBuilder<?> serverBuilder = ServerBuilder
				.forPort(grpcServerProperties.getPort());
		return new Runner(
				applicationContext,
				serverBuilder,
				healthStatusManager
		);
	}


    @Override
	public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
		this.applicationContext = applicationContext;
	}
}
