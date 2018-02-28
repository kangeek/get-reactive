package com.getset.webfluxascaller;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.web.reactive.function.client.WebClient;

@SpringBootApplication
public class WebfluxAsCallerApplication {

	public static void main(String[] args) {
		SpringApplication.run(WebfluxAsCallerApplication.class, args);
	}

}
