package com.yongs;

import org.junit.jupiter.api.Test;
import org.reactivestreams.Subscription;
import reactor.core.publisher.BaseSubscriber;
import reactor.core.publisher.Flux;

import java.util.concurrent.TimeUnit;

/**
 * @ClassName BackPressureTest
 * @Author Sawyer Yong
 * @Date 2020-01-16 10:24
 * @Description 背压测试
 */
public class BackPressureTest {


    /**
     *
     * 模拟一个很快的Publish和很慢的Consume, Comsume使用回压的方式通知Publish
     *
     * Flux.range是一个快的Publisher；
     * 在每次request的时候打印request个数；
     * 通过重写BaseSubscriber的方法来自定义Subscriber；
     * hookOnSubscribe定义在订阅的时候执行的操作；
     * 订阅时首先向上游请求1个元素；
     * hookOnNext定义每次在收到一个元素的时候的操作；
     * sleep 1秒钟来模拟慢的Subscriber；
     * 打印收到的元素；
     * 每次处理完1个元素后再请求1个。
     *
     * 我们可以通过自定义具有流量控制能力的Subscriber进行订阅。Reactor提供了一个BaseSubscriber，我们可以通过扩展它来定义自己的Subscriber。
     *
     * 假设，我们现在有一个非常快的Publisher——Flux.range(1, 6)，然后自定义一个每秒处理一个数据元素的慢的Subscriber，
     *
     * Subscriber就需要通过request(n)的方法来告知上游它的需求速度
     */
    @Test
    public void testBackpressure() {
        Flux.range(1, 6)
                .doOnRequest(n -> System.out.println("Request " + n + " values..."))
                .subscribe(new BaseSubscriber<Integer>() {
                    @Override
                    protected void hookOnSubscribe(Subscription subscription) {
                        System.out.println("Subscribed and make a request...");
                        request(1);
                    }

                    @Override
                    protected void hookOnNext(Integer value) {
                        try {
                            TimeUnit.SECONDS.sleep(1);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                        System.out.println("Get value [" + value + "]");
                        request(1);
                    }
                });
    }
}
