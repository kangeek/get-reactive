# 响应式Spring的道法术器

本项目源码来自[《响应式Spring的道法术器》](http://blog.csdn.net/get_set/article/details/79466657)系列文档。

这个系列的文章是为了记录下自己学习Spring响应式编程的所得，也希望能够帮助到更多的朋友。

原谅我标题党了，希望能借助蕴含古人智慧的道、法、术、器四个层面尽量全面地学习和介绍关于Spring响应式编程的方方面面。

*【道】响应式编程的概念、原则、特性与方法
*【法】响应式编程库的使用与原理
*【术】响应式关键技术与底层机制
*【器】实例了解Spring各种响应式组件

> 附Reactor参考文档（中文翻译，跟进更新）：http://htmlpreview.github.io/?https://github.com/get-set/reactor-core/blob/master-zh/src/docs/index.html。

----------

2017年9月，Spring Framework 5发布了其GA版本，这是自2013年12月以来的又一个大版本升级。除了一些人们期待已久的改进，最令人兴奋的新特性是它提供了完整的端到端响应式编程的支持。这是一种不同于Servlet的全新的编程范式和技术栈，它基于异步非阻塞的特性，能够借助EventLoop以少量线程应对高并发的访问，对微服务架构也颇有助益。不夸张的说，Spring 5使得Java世界拥有了Node.js那样骨骼惊奇的神器。

2018年3月1号，Spring Boot 2.0如约发布，也是一个大版本升级。

<img height=500em src="https://leanote.com/api/file/getImage?fileId=5a9dea18ab644159cf00065a"/>

从这个图就可以看出对支持Spring 5的Spring Boot 2.0来说，新加入的响应式技术栈是其主打核心特性。具体来说，Spring Boot 2支持的响应式技术栈包括如下：

  * Spring Framework 5提供的非阻塞web框架Spring Webflux；
  * 遵循响应式流规范的兄弟项目Reactor；
  * 支持异步I/O的Netty、Undertow等框架，以及基于Servlet 3.1+的容器（如Tomcat 8.0.23+和Jetty 9.0.4+）；
  * 支持响应式的数据访问Spring Data Reactive Repositories；
  * 支持响应式的安全访问控制Spring Security Reactive；
  * 等。
