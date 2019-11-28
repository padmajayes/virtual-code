package com.example.demo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

/**
 * SpringBootAuthenticationServiceApp
 *
 */
@SpringBootApplication(exclude = { SecurityAutoConfiguration.class })
public class SpringBootAuthenticationServiceApp
{

    @Bean
    public PasswordEncoder getPasswordEncoder() {
        return new BCryptPasswordEncoder();
    }

    public static void main( String[] args )
    {
        SpringApplication.run(SpringBootAuthenticationServiceApp.class, args);
    }
}
