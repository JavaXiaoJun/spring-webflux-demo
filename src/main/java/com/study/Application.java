package com.study;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.embedded.netty.NettyReactiveWebServerFactory;
import org.springframework.boot.web.reactive.server.ReactiveWebServerFactory;
import org.springframework.context.annotation.Bean;
import reactor.core.scheduler.Scheduler;
import reactor.core.scheduler.Schedulers;
import reactor.ipc.netty.resources.LoopResources;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@SpringBootApplication
public class Application {

    //loop线程数目
    @Value("${server.loopThreads}")
    private int loopThreads;

    //work线程数目
    @Value("${reactor.workThreads}")
    private int workThreads;

    public static void main(String[] args) {
        new SpringApplicationBuilder().sources(Application.class).run(args);
    }

    //使用netty作为webserver底层网络编程框架（也支持Tomcat，Jetty）
    @Bean
    public ReactiveWebServerFactory reactiveWebServerFactory() {
        NettyReactiveWebServerFactory factory = new NettyReactiveWebServerFactory();
        factory.addServerCustomizers(builder -> {
            builder.loopResources(LoopResources.create("research-http", loopThreads, true));
        });
        return factory;
    }

    //定义work线程池
    @Bean
    public Scheduler scheduler() {
        ExecutorService threadPool = Executors.newFixedThreadPool(workThreads);
        return Schedulers.fromExecutor(threadPool);
    }

}
