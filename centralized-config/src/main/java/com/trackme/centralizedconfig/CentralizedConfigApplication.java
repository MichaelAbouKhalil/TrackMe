package com.trackme.centralizedconfig;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.config.server.EnableConfigServer;

@SpringBootApplication
@EnableConfigServer
public class CentralizedConfigApplication {

    public static void main(String[] args) {
        SpringApplication.run(CentralizedConfigApplication.class, args);
    }

}
