package com.getset;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.junit.Test;
import org.reactivestreams.Subscription;
import reactor.core.publisher.BaseSubscriber;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;
import reactor.test.StepVerifier;

import java.time.Duration;
import java.util.*;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;
import java.util.stream.Stream;

public class Test_1_3 {

    /**
     * 1. 响应式之道 - 3 快速上手 - 1 lambda与函数式
     * <p>
     * 对Student进行排序的例子。
     */
    @Test
    public void StudentCompareTest() {
        @Data
        @AllArgsConstructor
        class Student {
            int id;
            String name;
            double height;
            double score;
        }

        List<Student> students = new ArrayList<>();
        students.add(new Student(10001, "张三", 1.73, 88));
        students.add(new Student(10002, "李四", 1.71, 96));
        students.add(new Student(10003, "王五", 1.85, 88));

        // 1. 通过实现类定义
        class StudentIdComparator<S extends Student> implements Comparator<S> {
            @Override
            public int compare(S s1, S s2) {
                return Integer.compare(s1.getId(), s2.getId());
            }
        }

        students.sort(new StudentIdComparator<>());
        System.out.println(students);

        // 2. 通过匿名内部类定义
        students.sort(new Comparator<Student>() {
            @Override
            public int compare(Student o1, Student o2) {
                return Double.compare(o1.getHeight(), o2.getHeight());
            }
        });
        System.out.println(students);

        // 3. 通过lambda定义
        students.sort((Student o1, Student o2) -> Double.compare(o1.getHeight(), o2.getHeight()));
        System.out.println(students);

        // 3.1 简化版lambda
//        students.sort(Comparator.comparingDouble(s -> s.getScore()));
        students.sort(Comparator.comparingDouble(Student::getScore));
        System.out.println(students);

    }

    /**
     * 1. 响应式之道 - 3 快速上手 - 2 Reactor
     * <p>
     * 基本的Flux和Mono序列，以及订阅。
     */
    @Test
    public void testSimpleFluxMono() {

        // 1. 用just声明数据流
        Flux.just(1, 2, 3, 4, 5, 6).subscribe(System.out::print);
        System.out.println();
        Flux.just(1, 2, 3, 4, 5, 6).subscribe(System.out::println, System.err::println, () -> System.out.println("Completed!"));
        Integer[] array = new Integer[]{1, 2, 3, 4, 5, 6};
        Flux.fromArray(array);
        List<Integer> list = Arrays.asList(array);
        Flux.fromIterable(list);
        Stream<Integer> stream = list.stream();
        Flux.fromStream(stream);
        Mono.just(1).subscribe(System.out::println);

        // 2. 只有完成或错误信号的数据流
        Flux.just();
        Flux.empty();
        Flux.error(new Exception("some error"));
        Mono.empty();
        Mono.justOrEmpty(Optional.empty());
        System.out.println("---Mono.error---");
        Mono.error(new Exception("some error")).subscribe(
                System.out::println,
                System.err::println,
                () -> System.out.println("Completed!"),
                s -> System.out.println("Subscribed!")
        );
    }

    /**
     * 1. 响应式之道 - 3 快速上手 - 2 Reactor
     * <p>
     * 使用StepVerifier进行单测。
     */
    @Test
    public void testViaStepVerifier() {
        StepVerifier.create(generateFluxFrom1To6())
                .expectNext(1, 2, 3, 4, 5, 6)
                .expectComplete()
                .verify();
        StepVerifier.create(generateMonoWithError())
                .expectErrorMessage("some error")
                .verify();
    }

    private Flux<Integer> generateFluxFrom1To6() {
        return Flux.just(1, 2, 3, 4, 5, 6);
    }

    private Mono<Integer> generateMonoWithError() {
        return Mono.error(new Exception("some error"));
    }

    /**
     * 1. 响应式之道 - 3 快速上手 - 2 Reactor
     * <p>
     * 测试基本的Operator。
     */
    @Test
    public void testSimpleOperators() throws InterruptedException {
        // map + filter
        StepVerifier.create(Flux.range(1, 6)
                .filter(i -> i % 2 == 1)
                .map(i -> i * i))
                .expectNext(1, 9, 25)
                .verifyComplete();

        // flatMap
        StepVerifier.create(
                Flux.just("flux", "mono")
                        .flatMap(s -> Flux.fromArray(s.split("\\s*"))
                                .delayElements(Duration.ofMillis(100)))
                        .doOnNext(System.out::print))
                .expectNextCount(8)
                .verifyComplete();

        // zip
        CountDownLatch countDownLatch = new CountDownLatch(1);
        Flux.zip(
                getZipDescFlux(),
                Flux.interval(Duration.ofMillis(200)))
//                .doOnComplete(countDownLatch::countDown)
                .subscribe(t -> System.out.println(t.getT1()), null, countDownLatch::countDown);
        countDownLatch.await(10, TimeUnit.SECONDS);
    }

    private Flux<String> getZipDescFlux() {
        String desc = "Zip two sources together, that is to say wait for all the sources to emit one element and combine these elements once into a Tuple2.";
        return Flux.fromArray(desc.split("\\s+"));
    }

    /**
     * 1. 响应式之道 - 3 快速上手 - 2 Reactor
     * <p>
     * 将同步阻塞调用转化为异步。
     */
    @Test
    public void testSyncToAsync() throws InterruptedException {
        CountDownLatch countDownLatch = new CountDownLatch(1);
        Mono.fromCallable(() -> getStringSync())
                .subscribeOn(Schedulers.elastic())
                .subscribe(System.out::println, null, countDownLatch::countDown);
        countDownLatch.await(10, TimeUnit.SECONDS);
    }

    private String getStringSync() {
        try {
            TimeUnit.SECONDS.sleep(2);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return "Hello, Reactor!";
    }

    /**
     * 1. 响应式之道 - 3 快速上手 - 2 Reactor
     * <p>
     * 错误处理，都整一个测试方法里了，囧。
     */
    @Test
    public void testErrorHandling() throws InterruptedException {
        // 0. 在订阅的时候进行错误处理
        Flux.range(1, 6)
                .map(i -> 10 / (i - 3))
                .map(i -> i * i)
                .subscribe(System.out::println, System.err::println);

        // 1. 遇到错误信号给出缺省值
        Flux.range(1, 6)
                .map(i -> 10 / (i - 3))
                .onErrorReturn(0)
                .map(i -> i * i)
                .subscribe(System.out::println, System.err::println);

        // 2. 捕获并执行一个异常处理方法或动态计算值顶替
        Flux.range(1, 6)
                .map(i -> 10 / (i - 3))
                .onErrorResume(e -> Mono.just(new Random().nextInt(6)))
                .map(i -> i * i)
                .subscribe(System.out::println, System.err::println);

        // 3. 捕获，并再包装为某一个业务相关的异常，然后再抛出业务异常
        Flux.range(1, 6)
                .map(i -> 10 / (i - 3))
                .onErrorMap(e -> new RuntimeException("被零除异常"))
                .map(i -> i * i)
                .subscribe(System.out::println, System.err::println);

        // 4. 只读地记录日志
        Flux.range(1, 6)
                .map(i -> 10 / (i - 3))
                .doOnError(e -> System.out.println("记录日志：" + e))
                .map(i -> i * i)
                .subscribe(System.out::println, System.err::println);

        // 5. 使用 finally 来清理资源，或使用 Java 7 引入的 "try-with-resource"，以下伪代码：
//        Flux.using(
//                () -> getResource(),    // 第一个参数获取资源
//                resource -> Flux.just(resource.getAll()),   // 第二个参数利用资源生成数据流
//                MyResource::clean   // 第三个参数最终清理资源
//        );

        // 6. 重试
        Thread.sleep(1000);
        Flux.range(1, 6)
                .map(i -> 10 / (3 - i))
                .retry(1)
                .subscribe(System.out::println, System.err::println);
        Thread.sleep(100);
    }

    /**
     * 1. 响应式之道 - 3 快速上手 - 2 Reactor
     * <p>
     * 测试回压。
     */
    @Test
    public void testBackpressure() {
        Flux.range(1, 6)
                .log()
                .subscribe(new BaseSubscriber<Integer>() {
                    @Override
                    protected void hookOnSubscribe(Subscription subscription) {
                        request(1);
                    }

                    @Override
                    protected void hookOnNext(Integer value) {
                        try {
                            TimeUnit.SECONDS.sleep(1);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                        request(1);
                    }
                });
    }

}
