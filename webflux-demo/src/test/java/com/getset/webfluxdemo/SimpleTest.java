package com.getset.webfluxdemo;

import com.getset.webfluxdemo.model.MyEvent;
import com.getset.webfluxdemo.model.User;
import org.junit.Test;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.Duration;
import java.util.concurrent.TimeUnit;

public class SimpleTest {
    /**
     * 请求一个普通的Endpoint。
     */
    @Test
    public void webClientTest1() throws InterruptedException {
        WebClient webClient = WebClient.create("http://localhost:8080");
        Mono<String> resp = webClient
                .get().uri("/hello")
                .retrieve()
                .bodyToMono(String.class);
        resp.subscribe(System.out::println);
        TimeUnit.SECONDS.sleep(1);
    }

    /**
     * 请求一个可以发出数据流的Endpoint。
     * 使用了一些不同的技巧和方法。
     */
    @Test
    public void webClientTest2() {
        WebClient webClient = WebClient.builder().baseUrl("http://localhost:8080").build();
        webClient
                .get().uri("/user")
                .accept(MediaType.APPLICATION_STREAM_JSON)
                .exchange()
                .flatMapMany(response -> response.bodyToFlux(User.class))
                .doOnNext(System.out::println)
                .blockLast();
    }

    /**
     * 请求一个可以发出SSE的Endpoint。
     */
    @Test
    public void webClientTest3() {
        WebClient webClient = WebClient.create("http://localhost:8080");
        webClient
                .get().uri("/times")
                .accept(MediaType.TEXT_EVENT_STREAM)
                .retrieve()
                .bodyToFlux(String.class)
                .log()
                .take(10)
                .blockLast();
    }

    /**
     * 请求一个可以接收数据流的Endpoint。
     */
    @Test
    public void webClientTest4() {
        Flux<MyEvent> eventFlux = Flux.interval(Duration.ofSeconds(1))
                .map(l -> new MyEvent(System.currentTimeMillis(), "message-" + l)).take(5);
        WebClient webClient = WebClient.create("http://localhost:8080");
        webClient
                .post().uri("/events")
                .contentType(MediaType.APPLICATION_STREAM_JSON)
                .body(eventFlux, MyEvent.class)
                .retrieve()
                .bodyToMono(Void.class)
                .block();
    }
}
