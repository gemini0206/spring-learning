package com.example.spring.grpc.server;

import io.grpc.BindableService;
import io.grpc.ServerBuilder;
import io.grpc.ServerServiceDefinition;
import io.grpc.health.v1.HealthCheckResponse;
import io.grpc.services.HealthStatusManager;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.util.CollectionUtils;

import java.lang.annotation.Annotation;
import java.util.Map;
import java.util.stream.Stream;

@Slf4j
public class Runner {
    private final ApplicationContext applicationContext;

    private final ServerBuilder<?> serverBuilder;

    private final HealthStatusManager healthStatusManager;

    public Runner(ApplicationContext applicationContext, ServerBuilder<?> serverBuilder, HealthStatusManager healthStatusManager) {
        this.applicationContext = applicationContext;
        this.serverBuilder = serverBuilder;
        this.healthStatusManager = healthStatusManager;
    }

    private void startup() {
        Runner runner = this;
        log.info("Starting gRPC Server ...");
        serverBuilder.addService(healthStatusManager.getHealthService());
        healthStatusManager.setStatus("grpc", HealthCheckResponse.ServingStatus.SERVING);
        getBeanNames(GrpcService.class, BindableService.class)
                .forEach(name -> {
                    BindableService srv = applicationContext.getBean(name, BindableService.class);
                    GrpcService grpcService = applicationContext.findAnnotationOnBean(name, GrpcService.class);
//                    ServerServiceDefinition serviceDefinition = GrpcServerRunner.this.bindInterceptors(
//                            srv.bindService(),
//                            grpcService,
//                            getServerInterceptors()
//                    );

                    //todo: inject jaeger tracer
                    //ServerTracingInterceptor tracingInterceptor = new ServerTracingInterceptor(TraceProvider.jaegerTracer(name));
                    //serverBuilder.addService(tracingInterceptor.intercept(serviceDefinition));

                    serverBuilder.addService(srv.bindService());
                    healthStatusManager.setStatus(
                            srv.bindService().getServiceDescriptor().getName(),
                            HealthCheckResponse.ServingStatus.SERVING
                    );

                    log.info("'{}' service has been registered.", srv.getClass().getName());
                });
    }

    private <T> Stream<String> getBeanNames(Class<? extends Annotation> annotationType, Class<T> beanType) {

        return Stream.of(applicationContext.getBeanNamesForType(beanType))
                .filter(name -> {
                    Map<String, Object> beansWithAnnotation = applicationContext.getBeansWithAnnotation(annotationType);

                    if (beansWithAnnotation.containsKey(name)) {
                        return true;
                    }

                    return false;
                });
    }
}
