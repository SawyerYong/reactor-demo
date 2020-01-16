package com.yongs;

import org.junit.jupiter.api.Test;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.core.publisher.SignalType;

import java.util.Random;
import java.util.concurrent.atomic.LongAdder;

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

    /**
     * 捕获，记录错误日志，然后继续抛出
     */
    @Test
    public void test05(){
        Flux.range(1, 6)
                .map(i -> 10/(3-i))
                .doOnError(e->{
                    System.out.println("出错了, 出错信息: "+e.getMessage());
                    e.printStackTrace();
                })
                .onErrorResume(e-> Mono.just(new Random().nextInt(6)))
                .subscribe(System.out::println,System.out::println);
    }



    /**
     * 捕获，记录错误日志，然后继续抛出  并使用 finally 来清理资源，
     */
    @Test
    public void test06(){

        Flux.range(1, 6)
                .map(i -> 10/(3-i))
                .doOnError(e->{
                    System.out.println("出错了, 出错信息: "+e.getMessage());
                    e.printStackTrace();
                })
                .doFinally(signalType -> {
                    if(signalType== SignalType.CANCEL){
                        // 触发取消事件
                    }
                    System.out.println("回收资源,signalType="+signalType);
                })
                .onErrorResume(e-> Mono.just(new Random().nextInt(6)))
                .subscribe(System.out::println,System.out::println);
    }


    /**
     * 出错重试, 即订阅者重庆重新获取一下资源流
     */
    @Test
    public void test07(){
        Flux.range(1, 6)
                .map(i -> 10/(3-i))
                .onErrorMap(e-> new Exception("出错",e))
                // 错误后尝试两次
                .retry(2)
                .subscribe(System.out::println,System.out::println);
    }

}
