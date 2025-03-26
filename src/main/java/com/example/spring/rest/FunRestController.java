package com.example.spring.rest;

import com.example.spring.grpc.HelloRequest;
import com.example.spring.grpc.HelloServiceGrpc;
import com.example.spring.grpc.client.GrpcClientConfig;
import com.example.util.Coach;
import io.grpc.StatusRuntimeException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController()
public class FunRestController {
    @Autowired
    @Qualifier("aquatic")
    private Coach myCoach;

    @Autowired
    private Coach anotherCoach;

    @Autowired
    GrpcClientConfig grpcClientConfig;

    @Value("${coach.name}")
    private String coachName;

    @Value("${team.name}")
    private String teamName;

    @GetMapping("/")
    public String sayHello() {
        return "Hello World";
    }

    @GetMapping("/workout")
    public String getDailyWorkout() {
        return myCoach.getDailyWorkout();
    }

    @GetMapping("/fortune")
    public String getDailyFortune() {
        return "Today is your lucky day.";
    }

    @GetMapping("/teaminfo")
    public String getTeamInfo() {
       return String.format("Coach %s team %s", coachName, teamName);
    }

    @GetMapping("/check")
    public String check() {
        return "Comparing beans: myCoach == anotherCoach," + (myCoach == anotherCoach);
    }

    @GetMapping("/test-grpc")
    public String testGrpc() {
        HelloServiceGrpc.HelloServiceBlockingStub stub = grpcClientConfig.helloServiceBlockingStub();
        try {
            return stub.hello(HelloRequest.newBuilder().setFirstName("sit").setLastName("incididunt cupidatat sed").build()).getGreeting();
        } catch (StatusRuntimeException e) {
            return "";
        }
    }
}
