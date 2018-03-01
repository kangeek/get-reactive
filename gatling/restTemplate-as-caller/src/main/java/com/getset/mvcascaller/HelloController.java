package com.getset.mvcascaller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

@RestController
public class HelloController {
    private final String TARGET_HOST = "http://localhost:8092";
    private RestTemplate restTemplate;

    public HelloController() {
        this.restTemplate = new RestTemplate();
    }

    @GetMapping("/hello/{latency}")
    public Mono<String> hello(@PathVariable int latency) {
//        return restTemplate.getForObject(TARGET_HOST + "/hello/" + latency, String.class);
        return Mono.fromCallable(() -> restTemplate.getForObject(TARGET_HOST + "/hello/" + latency, String.class))
                .subscribeOn(Schedulers.elastic());
    }
}
