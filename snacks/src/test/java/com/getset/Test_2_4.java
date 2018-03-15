package com.getset;

import org.junit.Test;
import reactor.core.publisher.Flux;
import reactor.core.scheduler.Schedulers;

import java.sql.Time;
import java.time.Duration;
import java.util.concurrent.TimeUnit;

public class Test_2_4 {

    @Test
    public void testScheduling() {
        Flux.range(0, 10)
//                .log()
                .publishOn(Schedulers.newParallel("myParrallel"))
//                .log()
                .subscribeOn(Schedulers.newElastic("myElastic"))
                .log()
                .blockLast();
    }

    @Test
    public void testDelayElements() {
        Flux.range(0, 10)
                .delayElements(Duration.ofMillis(10))
                .log()
                .blockLast();
    }

    @Test
    public void testParallelFlux() throws InterruptedException {
        Flux.range(1, 10)
                .parallel(2)
                .runOn(Schedulers.parallel())
//                .publishOn(Schedulers.parallel())
                .log()
                .subscribe();

        TimeUnit.MILLISECONDS.sleep(10);
    }
}
