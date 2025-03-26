package com.example.spring.grpc.server;

import io.grpc.inprocess.InProcessServerBuilder;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import io.grpc.services.HealthStatusManager;

@AutoConfiguration
//@ConditionalOnBean(annotation = GrpcService.class)
@EnableConfigurationProperties(GrpcServerProperties.class)
public class GrpcAutoConfiguration implements ApplicationContextAware {
    private ApplicationContext applicationContext;

    @Autowired
    private GrpcServerProperties grpcServerProperties;

    @Bean
    //	@ConditionalOnExpression("#{environment.getProperty('grpc.inProcessServerName','')!=''}")
	public Runner grpcInprocessServerRunner(HealthStatusManager
			healthStatusManager) {
		return new Runner(
				applicationContext,
				InProcessServerBuilder.forName(grpcServerProperties.getInProcessServerName()),
				healthStatusManager
		);
	}


    @Override
	public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
		this.applicationContext = applicationContext;
	}
}
