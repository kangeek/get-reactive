package com.getset.webfluxdemo;

import com.github.dockerjava.api.DockerClient;
import com.github.dockerjava.api.model.Event;
import com.github.dockerjava.core.DockerClientBuilder;
import com.github.dockerjava.core.command.EventsResultCallback;
import org.junit.Test;
import reactor.core.publisher.Flux;
import reactor.core.publisher.FluxSink;

import java.util.concurrent.TimeUnit;

public class DockerEventTest {
    @Test
    public void dockerEventToFlux() throws InterruptedException {
        collectDockerEvents().subscribe(System.out::println);
        TimeUnit.MINUTES.sleep(1);
    }

    private Flux<Event> collectDockerEvents() {
        DockerClient docker = DockerClientBuilder.getInstance().build();
        return Flux.create((FluxSink<Event> sink) -> {
            EventsResultCallback callback = new EventsResultCallback() {
                @Override
                public void onNext(Event event) {
                    sink.next(event);
                }
            };
            docker.eventsCmd().exec(callback);
        });
    }
}
