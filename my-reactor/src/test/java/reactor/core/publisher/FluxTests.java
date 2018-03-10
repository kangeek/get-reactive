package reactor.core.publisher;

import org.junit.Test;
import org.reactivestreams.Subscriber;
import org.reactivestreams.Subscription;

import static org.junit.Assert.*;

public class FluxTests {

    @Test
    public void fluxArrayAndMapTest() {
        Flux.just(1, 2, 3, 4, 5)
                .map(i -> i * i)
                .subscribe(new Subscriber<Integer>() {

                    @Override
                    public void onSubscribe(Subscription s) {
                        System.out.println("onSubscribe");
                        s.request(6);
                    }

                    @Override
                    public void onNext(Integer integer) {
                        System.out.println("onNext:" + integer);
                    }

                    @Override
                    public void onError(Throwable t) {

                    }

                    @Override
                    public void onComplete() {
                        System.out.println("onComplete");
                    }
                });
    }

    @Test
    public void lambdaSubscriberTest() {
        Flux.just(1, 2, 3, 4, 5)
                .map(i -> i * 2)
                .subscribe(
                        System.out::println,
                        System.err::println,
                        () -> System.out.println("Completed.")
//                        subscription -> subscription.request(3)
                );
    }

}