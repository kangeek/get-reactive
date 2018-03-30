package com.getset.webfluxdemo.repository;

import com.getset.webfluxdemo.model.DockerEvent;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.data.mongodb.repository.Tailable;
import reactor.core.publisher.Flux;

public interface DockerEventMongoRepository extends ReactiveMongoRepository<DockerEvent, String> {
    @Tailable
    Flux<DockerEvent> findByStatus(String status);

    @Tailable
    Flux<DockerEvent> findByTypeAndFrom(String type, String from);

    @Tailable
    Flux<DockerEvent> findBy();
}
