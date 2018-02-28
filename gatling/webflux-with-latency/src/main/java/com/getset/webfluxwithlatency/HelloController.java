package com.getset.webfluxwithlatency;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

import java.time.Duration;

@RestController
public class HelloController {
    @GetMapping("/hello/{latency}")
    public Mono<String> hello(@PathVariable int latency) {
        return Mono.just("Welcome to reactive world ~")
                .delayElement(Duration.ofMillis(latency));
    }
}
