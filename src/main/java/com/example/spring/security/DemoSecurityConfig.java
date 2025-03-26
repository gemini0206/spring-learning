package com.example.spring.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class DemoSecurityConfig {

    @Bean
    public InMemoryUserDetailsManager userDetailsManager() {
        return new InMemoryUserDetailsManager(
                User.builder().username("john").password("{noop}test123").roles("EMPLOYEE").build(), // format: {id}encodedPassword id: noop|bcrypt|...
                User.builder().username("marry").password("{noop}test123").roles("EMPLOYEE", "MANAGER").build() // noop: plain text password
        );
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
//        http.authorizeHttpRequests(configure ->
//                configure.requestMatchers(HttpMethod.GET, "/api/students").hasRole("EMPLOYEE")
//                        .requestMatchers(HttpMethod.GET, "/api/students/**").hasRole("MANAGER")
//        );

//        http.httpBasic(Customizer.withDefaults());

        http.csrf(AbstractHttpConfigurer::disable);

        return http.build();
    }
}
