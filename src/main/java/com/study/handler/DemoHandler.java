package com.study.handler;

import com.study.model.Product;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

/**
 * 在传统命令式的编程风格中，线程的执行会被堵塞，直到接收到数据。这使得数据在实际返回之前线程必须进行等待。
 * 而在 Reactive 编程中，我们定义一个流用来发送数据并数据返回时所执行的操作。使用这种方法线程是不会被堵塞的，当数据返回时框架会选择一个可用的线程进行下一步处理。
 */
@Service
public class DemoHandler {

    public Mono<Product> handleGetProduct(int id) {
        return Mono.fromCallable(() ->{
            Thread.sleep(1000 * 10);
           return new Product(id);
        });
    }

}
