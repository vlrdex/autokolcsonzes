package com.example.autokolcsonzes.Config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class WebSecurityConfig {
    private static final boolean isAdmin=true;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {


        if(isAdmin){
            http.authorizeHttpRequests(auth ->
                    auth.anyRequest().permitAll()
            );
        }else {
            http.authorizeHttpRequests(auth-> auth
                    .requestMatchers("/admin/**").denyAll()
                    .anyRequest().permitAll()
            );
        }


        return http.build();
    }

    public static boolean getIsAdmin(){
        return isAdmin;
    }

}
