package com.getset.webfluxascaller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@RestController
public class HelloController {
    private final String TARGET_HOST = "http://localhost:8092";
    private WebClient webClient;

    public HelloController() {
        this.webClient = WebClient.builder().baseUrl(TARGET_HOST).build();
    }

    @GetMapping("/hello/{latency}")
    public Mono<String> hello(@PathVariable int latency) {
        return webClient
                .get().uri("/hello/" + latency)
                .exchange()
                .flatMap(clientResponse -> clientResponse.bodyToMono(String.class));
    }
}
