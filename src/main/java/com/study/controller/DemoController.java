package com.study.controller;

import com.study.model.Product;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Scheduler;

@RestController
public class DemoController {

    @Autowired
    private Scheduler scheduler;

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
        }).subscribeOn(scheduler);
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


}
