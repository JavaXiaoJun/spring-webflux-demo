package com.study.controller;

import com.study.handler.DemoHandler;
import com.study.model.Product;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Scheduler;

@RestController
public class DemoController {

    @Autowired
    private Scheduler scheduler;

    @Autowired
    private DemoHandler handler;

    /**
     * for spring webflux
     * @param request
     * @return
     */
    @RequestMapping(value = "/demo", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public Mono<Product> demo(ServerHttpRequest request) {

        Mono<Product> result = Mono.fromCallable(() ->{
            MultiValueMap<String, String> params = request.getQueryParams();
            String type = params.getFirst("messagetype");
            String body = params.getFirst("messagebody");
            System.out.println(type + ":" + body);
            Thread.sleep(20);
            return new Product(0);
        }).subscribeOn(scheduler).onErrorReturn(new Product(0));
        return result;
    }

    /**
     * for springmvc
     * @param messagetype
     * @param messagebody
     * @return
     * @throws InterruptedException
     */
    @RequestMapping(value = "/demo1", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @ResponseBody
    public String demo1(String messagetype,String messagebody) throws InterruptedException {

        System.out.println(messagetype + ":" + messagebody);
        Thread.sleep(20);
        return "0";
    }

    @RequestMapping(value = "/demo2", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @ResponseBody
    public Mono<Product> demo2() throws InterruptedException {
        Mono<Product> product  = handler.handleGetProduct(10);
        System.out.println("test !!!!!!!!!!!!!!!!!!!");
        return product ;
    }


}
