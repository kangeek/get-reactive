package com.getset;

import org.junit.Test;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;
import reactor.test.publisher.PublisherProbe;
import reactor.test.publisher.TestPublisher;

import java.time.Duration;

public class Test_2_6 {
    private Flux<String> appendBoomError(Flux<String> origin) {
        return origin.concatWith(Mono.error(new RuntimeException("boom")));
    }

    @Test
    public void testWithStepVerifier() {
        Flux<String> source = Flux.just("foo", "bar");

        StepVerifier.create(
                appendBoomError(source))
                .expectNext("foo")
                .expectNext("bar")
                .expectErrorMessage("boom")
                .verify();
    }

    @Test
    public void testVirtualTime() {
        StepVerifier.withVirtualTime(() -> Mono.delay(Duration.ofDays(1)))
                .expectSubscription()   // 1
                .expectNoEvent(Duration.ofDays(1))  // 2
                .expectNext(0L)
                .verifyComplete();
    }

    private Mono<String> executeCommand(String command) {
        return Mono.just(command + " DONE");
    }

    private Mono<Void> processOrFallback(Mono<String> commandSource, Mono<Void> doWhenEmpty) {
        return commandSource
                .flatMap(command -> executeCommand(command).then())
                .switchIfEmpty(doWhenEmpty);
    }

    @Test
    public void testWithPublisherProbe() {
        PublisherProbe<Void> probe = PublisherProbe.empty();

        StepVerifier.create(processOrFallback(Mono.empty(), probe.mono()))
                .verifyComplete();

        probe.assertWasSubscribed();
        probe.assertWasRequested();
        probe.assertWasNotCancelled();
    }

    @Test
    public void testWithTestPublisher() {
        TestPublisher<Integer> testPublisher = TestPublisher.<Integer>create().emit(1, 2, 3);
        StepVerifier.create(testPublisher.flux().map(i -> i * i))
                .expectNext(1, 4, 9)
                .expectComplete();
    }
}
