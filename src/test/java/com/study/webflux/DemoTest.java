package com.study.webflux;

import com.newegg.ec.ncommon.http.client.RestfullClient;
import com.newegg.ec.ncommon.http.client.factory.AbstractClientFactory;
import com.newegg.ec.ncommon.http.client.factory.jersey.DefaultJerseyClientFactory;
import org.junit.Test;
import org.springframework.http.MediaType;

import javax.ws.rs.client.Entity;
import javax.ws.rs.core.MultivaluedHashMap;
import javax.ws.rs.core.MultivaluedMap;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.*;

/**
 * 测试webflux和springmvc的响应差异
 * 50个user并发请求，每个user请求500次，计算平均响应时间
 * 测试结果（5次取平均值）：（代码中有20ms的sleep）
 *   webflux ：21841867 ns
 *   springmvc ： 98561012 ns
 *
 * 当增加到100个user时，springmvc的表现就更差了，所以在高并发的场景下webflux表现远好于springmvc
 */
public class DemoTest {

    private static final AbstractClientFactory factory = new DefaultJerseyClientFactory(100, 10000, 10000);
    private static final int size = 50;
    ExecutorService pool = Executors.newFixedThreadPool(100);

    /**
     * test webflux
     * @throws Exception
     */
    @Test
    public void testWebFlux() throws Exception {

        MultivaluedMap<String, Object> param = new MultivaluedHashMap<>();
        param.add("messagetype", "test");
        param.add("messagebody", "leo");

        RestfullClient client = new RestfullClient(factory);

        List<Future<Long>> list = new ArrayList(size);
        for(int i = 0 ; i < size ; i ++ ) {
            list.add(pool.submit(task(client,"demo",param)));
        }
        long total = 0;
        for(Future<Long> future : list){
            long num =  future.get();
            total = total + num;
        }
        System.out.println("average : " + total/size);

    }


    /**
     * test springmvc
     * @throws Exception
     */
    @Test
    public void testSpringMvc() throws Exception {
        MultivaluedMap<String, Object> param = new MultivaluedHashMap<>();
        param.add("messagetype", "test");
        param.add("messagebody", "leo");

        RestfullClient client = new RestfullClient(factory);

        List<Future<Long>> list = new ArrayList(size);
        for(int i = 0 ; i < size ; i ++ ) {
            list.add(pool.submit(task(client,"demo1",param)));
        }
        long total = 0;
        for(Future<Long> future : list){
            long num =  future.get();
            total = total + num;
        }
        System.out.println("average : " + total/size);
    }

    public Callable<Long> task( RestfullClient client ,String target , MultivaluedMap<String, Object> param){

        return new Callable<Long>() {
            @Override
            public Long call() throws Exception {
                long num = 0;
                for(int i = 0 ; i < 500 ; i++){
                    long start = System.nanoTime();
                    client.post("http://localhost:8080", target ,null, param, Entity.entity(new String("string entity"), MediaType.APPLICATION_JSON_UTF8_VALUE));
                    long time = System.nanoTime() - start;
                    num = num + time;
                }
                System.out.println(Thread.currentThread().getName() + " avrage time : " + num/500);
                return  num/500;
            }
        };
    }


}
