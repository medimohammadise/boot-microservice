package com.example.rservationclient;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.netflix.zuul.EnableZuulProxy;

@EnableZuulProxy  //just blindly forwards requests from edge server to back-end
@SpringBootApplication
@EnableDiscoveryClient
public class RservationClientApplication {

	public static void main(String[] args) {
		SpringApplication.run(RservationClientApplication.class, args);
	}
}
