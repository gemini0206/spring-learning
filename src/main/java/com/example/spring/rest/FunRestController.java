package com.example.spring.rest;

import com.example.util.Coach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController()
public class FunRestController {
    @Autowired
//    @Qualifier("trackCoach")
    private Coach myCoach;

    @Autowired
    private Coach anotherCoach;

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
}
