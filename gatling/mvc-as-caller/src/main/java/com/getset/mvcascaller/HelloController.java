package com.getset.mvcascaller;

import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

@RestController
public class HelloController {
    private final String TARGET_HOST = "http://localhost:8092";
    private RestTemplate restTemplate;

    public HelloController(RestTemplateBuilder restTemplateBuilder) {
        this.restTemplate = restTemplateBuilder.build();
    }

    @GetMapping("/hello/{latency}")
    public String hello(@PathVariable int latency) {
        return restTemplate.getForObject(TARGET_HOST + "/hello/" + latency, String.class);
    }
}
