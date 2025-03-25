package com.example.util;

import jakarta.annotation.PostConstruct;
import jakarta.annotation.PreDestroy;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Primary;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

@Component
//@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
@Scope(ConfigurableBeanFactory.SCOPE_SINGLETON)
@Primary
public class CricketCoach implements Coach {

    @Override
    public String getDailyWorkout() {
       return "Practice fast bowling for 15 minutes";
    }

    @PostConstruct
    public void doMyStartupStuff() {
        System.out.println("In doMyStartupStuff: " + getClass().getSimpleName());
    }

    @PreDestroy
    public void doMyCleanStuff() {
        System.out.println("In doMyCleanStuff: " + getClass().getSimpleName());
    }
}
