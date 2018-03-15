package com.getset;

import org.junit.Test;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Hooks;
import reactor.core.publisher.Mono;

import java.time.Duration;

public class Test_2_7 {
    private Mono<Integer> getMonoWithException() {
        return Flux.<Integer>range(1, 5)
                .map(i -> i * i)
                .filter(i -> (i % 2) == 0)
                .single();
    }

    @Test
    public void checkBugWithGlobalHook() {
        Hooks.onOperatorDebug();
        getMonoWithException()
                .subscribe();
    }
    @Test
    public void checkBugWithCheckPoint() {
        getMonoWithException()
                .checkpoint()
                .subscribe();
    }
    @Test
    public void checkBugWithCheckPoint2() {
        getMonoWithException()
                .checkpoint("I_HATE_BUGS")
                .subscribe();
    }

    @Test
    public void testLog1() {
        Flux.range(1, 10)
                .log()
//                .delayElements(Duration.ofMillis(10))
                .take(3).blockLast();
    }
    @Test
    public void testLog2() {
        Flux.range(1, 10)
                .delayElements(Duration.ofMillis(10))
                .log()
                .take(3).blockLast();
    }
}
