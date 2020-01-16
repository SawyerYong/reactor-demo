package com.yongs;

import org.junit.jupiter.api.Test;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.Random;

/**
 * @ClassName ReactorErrorTest
 * @Author Sawyer Yong
 * @Date 2020-01-16 9:35
 * @Description Reactor中对于异常的先关处理
 */
public class ReactorErrorTest {
    /**
     * 异常信息 抛出异常 : java.lang.ArithmeticException: / by zero
     */
    @Test
    public void test01(){
        Flux.range(1, 6)
                .map(i -> 10/(3-i))
                .subscribe(System.out::println,System.out::println);

    }



    /**
     * 异常信息  捕获并返回一个静态的缺省值, 遇到异常返回 0
     */
    @Test
    public void test02(){
        Flux.range(1, 6)
                .map(i -> 10/(3-i))
                .onErrorReturn(0)
                .subscribe(System.out::println,System.out::println);

    }

    /**
     * 异常信息  捕获并执行一个异常处理方法或计算一个候补值来顶替
     *
     * 使用场景: 如调用外部服务失败则使用Cache来进行候补处理
     */
    @Test
    public void test03(){
        Flux.range(1, 6)
                .map(i -> 10/(3-i))
                .onErrorResume(e-> Mono.just(new Random().nextInt(6)))
                .subscribe(System.out::println,System.out::println);

    }

    /**
     * 捕获，并再包装为某一个业务相关的异常，然后再抛出业务异常
     * 即将异常进行自定义异常包装
     */
    @Test
    public void test04(){
        // 方法一 使用onErrorMap
        Flux.range(1, 6)
                .map(i -> 10/(3-i))
                .onErrorMap(e-> new Exception("出错",e))
                .subscribe(System.out::println,System.out::println);

        // 方法二 使用onErrorResume
        Flux.range(1, 6)
                .map(i -> 10/(3-i))
                .onErrorResume(e-> Mono.error(new Exception("出错",e)))
                .subscribe(System.out::println,System.out::println);

    }

}
