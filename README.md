## 什么是响应式编程框架
>  响应式框架的诞生是为了提高CPU利用率，对于IO相对于占主导的Service是非常适合的，IO主导的service经常会因为等待IO而阻塞线程，导致线程数量上升，造成操作系统维护线程句柄的资源浪费。
   而纯异步的开发模型能够让所有的操作异步化（主要是针对IO操作的异步化）。

## 响应式编程框架适用场景
>  如果一个业务service无法应对高并发而迫使部署大量instance，这种service可以使用响应式框架来提高资源的利用率，减少service instance的部署量节约资源。
   对于并发不高的service，可能无法直接从响应式框架中直接提升性能，但是对于这些service，可以集中在两台server（容灾）上就可以承载业务。

## 响应式编程框架推荐
>  WebFlux框架 + Reactor + Netty。
   <img src="./docs/images/create.png"/>

## WebFlux framework介绍
>  WebFlux frameword是Spring5第一代响应式编程框架，官方文档：https://docs.spring.io/spring/docs/5.0.0.BUILD-SNAPSHOT/spring-framework-reference/html/web-reactive.html
   架构上spring-webflux与spring-webmvc同级，是spring-webmvc的替代方案，底层网络模型脱离Servlet Api，采用了基于NIO的网络编程框架，支持包括Tomcat，Jetty，Netty等。
   spring-webflux依然沿用了与spring-webmvc相同的Controller注解和路由方式，对于旧项目迁移至新项目中带来了便利，中间层的业务代码由Reactive Stream方式管理，
   Reactive Streams默认采用Reactor框架（Reactor 入门），同时还支持另一款相对庞大的Reactive Stream框架RxJava。

## WebFlux 与 Spring MVC差异对比
>  Spring MVC依然还是沿用Servlet编程模式，Servlet编程模式屏蔽了底层IO模型，所以很多Servlet容器都支持NIO和BIO等多种模式可选，但是Servlet对于线程模型的控制力度很粗。
   这种模型的好处是request线程和loop线程进行了有效的隔离，即便是业务代码阻塞也完全不影响loop线程的运行，坏处是线程利用率低下，并发request数越大需要的线程越多，
   极大的影响了服务的极限性能。WebFlux模式的优势不是在于底层是否采用了NIO还是BIO，而是在上层替换了旧的Servlet线程模型。既然旧模型的问题在于用户无法使用Loop线程，
   所以WebFlux直接将Controller移交到Loop线程中，所以在Controller层返回的对象必须用Mono<T>或者Flux<T>包裹。这样做的好处在于允许用户在Loop线程中进行一些快速的非阻塞的操作，
   比如定义响应式编程模型对象等，不阻塞Loop线程，并绑定Scheduler，保证响应式编程模型能在新的线程中执行，提高并发性能。