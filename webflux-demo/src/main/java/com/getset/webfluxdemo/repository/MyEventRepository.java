package com.getset.webfluxdemo.repository;

import com.getset.webfluxdemo.model.MyEvent;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.data.mongodb.repository.Tailable;
import reactor.core.publisher.Flux;

public interface MyEventRepository extends ReactiveMongoRepository<MyEvent, Long> {
    @Tailable
    Flux<MyEvent> findBy();
}
