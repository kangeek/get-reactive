package reactor.core.publisher;

import org.reactivestreams.Publisher;
import org.reactivestreams.Subscriber;
import org.reactivestreams.Subscription;

import java.util.function.Consumer;
import java.util.function.Function;

public abstract class Flux<T> implements Publisher<T> {

    public abstract void subscribe(Subscriber<? super T> s);

    public void subscribe(Consumer<? super T> consumer, Consumer<? super Throwable> errorConsumer, Runnable completeConsumer, Consumer<? super Subscription> subscriptionConsumer) {
        subscribe(new LambdaSubscriber<>(consumer, errorConsumer, completeConsumer, subscriptionConsumer));
    }

    public void subscribe(Consumer<? super T> consumer) {
        subscribe(consumer, null, null, null);
    }

    public void subscribe(Consumer<? super T> consumer, Consumer<? super Throwable> errorConsumer) {
        subscribe(consumer, errorConsumer, null, null);
    }

    public void subscribe(Consumer<? super T> consumer, Consumer<? super Throwable> errorConsumer, Runnable completeConsumer) {
        subscribe(consumer, errorConsumer, completeConsumer, null);
    }

    public static <T> Flux<T> just(T... data) {
        return new FluxArray<>(data);
    }
    public <V> Flux<V> map(Function<? super T, ? extends V> mapper) {
        return new FluxMap<>(this, mapper);
    }
}
