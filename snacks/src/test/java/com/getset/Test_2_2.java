package com.getset;

import com.getset.chapter_2_2.MyEventListener;
import com.getset.chapter_2_2.MyEventSource;
import org.junit.Test;
import reactor.core.publisher.Flux;

import java.util.Date;
import java.util.Random;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

public class Test_2_2 {
    @Test
    public void testGenerate1() {
        final AtomicInteger count = new AtomicInteger(1);
        Flux.generate(sink -> {
            sink.next(count.get() + " : " + new Date());
            try {
                TimeUnit.SECONDS.sleep(1);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            if (count.getAndIncrement() >= 5) {
                sink.complete();
            }
        }).subscribe(System.out::println);
    }

    @Test
    public void testGenerate2() {
        Flux.generate(
                () -> 1,
                (count, sink) -> {
                    sink.next(count + " : " + new Date());
                    try {
                        TimeUnit.SECONDS.sleep(1);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    if (count >= 5) {
                        sink.complete();
                    }
                    return count + 1;
                }).subscribe(System.out::println);
    }

    @Test
    public void testGenerate3() {
        Flux.generate(
                () -> 1,
                (count, sink) -> {
                    sink.next(count + " : " + new Date());
                    try {
                        TimeUnit.SECONDS.sleep(1);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    if (count >= 5) {
                        sink.complete();
                    }
                    return count + 1;
                }, System.out::println)
                .subscribe(System.out::println);
    }

    @Test
    public void testCreate() throws InterruptedException {
        MyEventSource eventSource = new MyEventSource();
        Flux.create(sink -> {
                    eventSource.register(new MyEventListener() {
                        @Override
                        public void onNewEvent(MyEventSource.MyEvent event) {
                            sink.next(event);
                        }

                        @Override
                        public void onEventStopped() {
                            sink.complete();
                        }
                    });
                }
        ).subscribe(System.out::println);

        for (int i = 0; i < 20; i++) {
            Random random = new Random();
            TimeUnit.MILLISECONDS.sleep(random.nextInt(1000));
            eventSource.newEvent(new MyEventSource.MyEvent(new Date(), "Event-" + i));
        }
        eventSource.eventStopped();
    }


}
