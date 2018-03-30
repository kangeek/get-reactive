package com.getset.webfluxdemo.controller;

import com.getset.webfluxdemo.model.DockerEvent;
import com.getset.webfluxdemo.repository.DockerEventMongoRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;

@Slf4j
@RestController
public class DockerEventController {
    private DockerEventMongoRepository dockerEventMongoRepository;

    public DockerEventController(DockerEventMongoRepository dockerEventMongoRepository) {
        this.dockerEventMongoRepository = dockerEventMongoRepository;
    }

    @GetMapping(value = "/docker/events", produces = MediaType.APPLICATION_STREAM_JSON_VALUE)
    public Flux<DockerEvent> dockerEventStream() {
        return dockerEventMongoRepository.findBy();
    }

    @GetMapping(value = "/docker/{type}/{from}", produces = MediaType.APPLICATION_STREAM_JSON_VALUE)
    public Flux<DockerEvent> dockerEventStream(@PathVariable("type") String type, @PathVariable("from") String from) {
        return dockerEventMongoRepository.findByTypeAndFrom(type, from);
    }

    @GetMapping(value = "/docker/events/{status}", produces = MediaType.APPLICATION_STREAM_JSON_VALUE)
    public Flux<DockerEvent> dockerEventStream(@PathVariable String status) {
        return dockerEventMongoRepository.findByStatus(status);
    }
}
