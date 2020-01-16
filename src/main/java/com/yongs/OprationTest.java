package com.yongs;

import org.junit.jupiter.api.Test;
import reactor.core.publisher.Flux;

import java.util.Arrays;
import java.util.function.Function;

/**
 * @ClassName OprationTest
 * @Author Sawyer Yong
 * @Date 2020-01-16 16:35
 * @Description 说点什么吧~
 */
public class OprationTest {

    /** transform可以将一段操作链打包为一个函数式。
     * 这个函数式能在组装期将被封装的操作符还原并接入到调用transform的位置。这样做和直接将被封装的操作符加入到链上的效果是一样的
     * 示例:
     * 将c之外的元素转换成大写
     */
    @Test
    public void test01(){
        Flux.fromIterable(Arrays.asList("a","b","c","d"))
                .doOnNext(System.out::println)
                .filter(str-> !str.equals("b"))
                .map(str->str.toUpperCase())
                .subscribe(str-> System.out.println("after upperCase : "+str));

    }


    /**
     * 将c之外的元素转换成大写
     *
     * 使用transform打包字符串
     */
    @Test
    public void test02(){
        //创建一个操作符集
        Function<Flux<String>,Flux<String >> fluxFluxFunction =
                f -> f.filter(str-> !str.equals("b"))
                        .map(str->str.toUpperCase());

        // 使用transform使用操作符集
        Flux.fromIterable(Arrays.asList("a","b","c","d"))
                .doOnNext(System.out::println)
                .transform(fluxFluxFunction)
                .subscribe(str-> System.out.println("after upperCase : "+str));

    }

    /**
     * compose 操作符与 transform 类似，也能够将几个操作符封装到一个函数式中。
     * 主要的区别就是，这个函数式是针对每一个订阅者起作用的。这意味着它对每一个 subscription 可以生成不同的操作链
     */
    @Test
    public void test03(){

    }
}
