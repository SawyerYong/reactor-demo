package com.yongs;

import org.junit.jupiter.api.Test;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.time.Duration;
import java.util.Arrays;

/**
 * @ClassName ReactorTest01
 * @Author Sawyer Yong
 * @Date 2020-01-15 16:40
 * @Description 说点什么吧~
 */
public class ReactorTest01 {

    @Test
    public void test01(){
        System.out.println("ste");
    }

    /**
     * 常见的声明发布者的方式
     * Flux.just()以及Mono.just()
     */
    @Test
    public void test02(){
        Flux.just(1,2,3,4,5);

        Mono.just(1);
    }

    /**
     * Flux另外的声明方式
     * @see Flux
     */
    @Test
    public void test03(){
        // 通过数据声明
        Integer[] array = new Integer[]{1,2,3,4,5};
        Flux.fromArray(array);

        // 通过集合声明
        Flux.fromIterable(Arrays.asList(array));

        // 通过Stream声明
        Flux.fromStream(Arrays.asList(array).stream());

    }

    /**
     * map操作可以将数据元素进行转换/映射，得到一个新元素
     */
    @Test
    public void test04(){
        StepVerifier.create(Flux.range(1, 6)
                .map(i -> i * i))
                .expectNext(1, 4, 9, 16, 25, 36)
                .expectComplete();
    }

    /**
     * latMap操作可以将每个数据元素转换/映射为一个流，然后将这些流合并为一个大的数据流
     */
    @Test
    public void test05(){
        String[] split = "flux".split("\\s*");
        Arrays.stream(split).forEach(System.out::println);

        StepVerifier.create(
                Flux.just("flux", "mono")
                        .flatMap(s -> Flux.fromArray(s.split("\\s*"))
                                .delayElements(Duration.ofMillis(100)))
                        .doOnNext(System.out::print))
                .expectNextCount(8)
                .verifyComplete();
    }

    /**
     * filter接受一个Predicate的函数式接口为参数，这个函数式的作用是进行判断并返回boolean
     */
    @Test
    public void test06(){
        // 获取流中基数的平方
        StepVerifier.create(Flux.range(1, 6)
                .filter(i -> i % 2 == 1)
                .map(i -> i * i)
                .doOnNext(System.out::print))
                .expectNext(1, 9, 25)
                .verifyComplete();

    }

}
