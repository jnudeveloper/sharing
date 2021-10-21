## 网络、BIO、NIO、Netty
- IO、网络、netty的内容都很多，这里只是说一些入门的概念，帮助你们以后理解代码。
- IO即输入输出，涉及外设的读写都可以称为IO。这次我们着重网络IO，即网卡的读写。
- 模型、设计模式等，都可以理解为最佳实践，即比较好的可以重复使用的方法、方式，不要被高大上的名字迷惑了。
- 推荐书: 《netty权威指南 李林锋著》[电子版](https://github.com/many-books/study-book/blob/master/Netty%E6%9D%83%E5%A8%81%E6%8C%87%E5%8D%97%20PDF%E7%94%B5%E5%AD%90%E4%B9%A6%E4%B8%8B%E8%BD%BD%20%E5%B8%A6%E7%9B%AE%E5%BD%95%E4%B9%A6%E7%AD%BE%20%E5%AE%8C%E6%95%B4%E7%89%88.pdf)

### IO模型（操作系统提供的机制，可以说是操作系统层面IO的最佳实践）
- [IO模型-使用c语言描述](https://zhuanlan.zhihu.com/p/54580385)
- 基本IO模型（《UNIX网络编程》里面总结的内容）
    - 阻塞IO
    - 非阻塞IO
    - IO多路复用
    - 信号驱动IO
    - 异步IO
- 阻塞IO
```
应用程序发起系统调用recv        ---->    内核未准备好数据
中间一直阻塞                           内核已经准备好数据
                                        复制数据
应用程序处理数据               <----      复制完成
```

- 非阻塞IO
```
应用程序发起系统调用recv        ---->    内核未准备好数据
应用程序做其他事情              <----    立即返回
应用程序发起系统调用recv        ---->    内核未准备好数据
应用程序做其他事情              <----    立即返回
应用程序发起系统调用recv        ---->    内核已经准备好数据
                                        复制数据
应用程序处理数据                <----      复制完成
```
由上面的阻塞IO和非阻塞IO，我们可以知道，是否阻塞取决于数据未准备好的时候，应用程序是否等待。
接下来看下面BIO的例子。

- IO多路复用
```
应用程序发起系统调用select(多个IO事件) ---->    内核未准备好数据
阻塞                                         内核未准备好数据
应用程序查看就绪的IO事件              <----    内核已经准备好数据
应用程序发起recv（就绪的IO事件）       ---->      复制数据
应用程序处理数据                     <----      复制完成
```
非阻塞IO通过立即返回，来使得应用程序可以做其他的事情。而IO多路复用通过监听多个IO事件，使得阻塞的时间变短。
因为监听多个IO事件，总有一个是就绪可用的。
接下来看下面NIO的例子。

- 异步IO
```
应用程序发起异步IO系统调用（aio_read） ---->         内核未准备好数据
应用程序做其他事情                    <----         立即返回
应用程序做信号处理                    <--返回信号--  内核已经准备好数据
                                                 复制数据
                                                 复制完成
                                    <--返回信号-- 处理数据（程序在aio_read里面已经提交）
```
所谓异步，也就是由操作系统处理数据，无需用户程序发起recvfrom系统调用和无需用户程序调用数据处理程序。
因为异步IO，事情全部都由操作系统去做了，所以不会有应用程序阻塞的情况。所以阻塞IO一般都是指同步阻塞IO。
接下来看下面NIO2.0的例子。

- 信号驱动IO（有了异步IO，这个基本没什么用，留给你们自己思考）

### BIO 同步阻塞IO
- 一般我们说的阻塞IO必定是同步的（等一下在IO模型里面会说明）
- 字节流（InputStream、OutputStream）、字符流（Reader、Writer）
- [文件BIO例子](https://github.com/jnudeveloper/sharing/tree/master/2021/io_netty/src/bio/BIODemo.java)
- [网络BIO例子](https://github.com/jnudeveloper/sharing/tree/master/2021/io_netty/src/bio/NetBIODemo.java)
- 面向对象设计相关问题
    - 内存空间管理的封装问题: 封装数组的操作，使得数据可以动态拓展、在内存空间中可以不连续等。（Buffer、put、get）
    - 输入输出操作的封装问题: 几乎每次操作都涉及到写和读，不如把两个操作封装到一个类里面。（Channel、read、write）
    - 应用层协议和业务逻辑的封装、解耦问题
- 高性能、高可用相关问题:
    - 阻塞问题: java没有直接的api来使得io变成非阻塞，因为IO复用已经从另一个角度解决了问题。想了解怎么解决非阻塞问题，可以看 [IO模型-使用c语言描述](https://zhuanlan.zhihu.com/p/54580385) 里面的c函数接口。
    - IO复用问题: 通过监听多个IO事件，使得阻塞的时间变短。（Selector、select、poll、epoll）
    - 线程管理问题: 在一个线程里面进行所有操作，性能不好，也不能利用现代多核多线程处理器的优点。(每个请求一个线程、线程池、线程模型)

### NIO(New IO, Non-blocked IO) JDK1.4 非阻塞IO和IO多路复用
- [文件NIO例子](https://github.com/jnudeveloper/sharing/tree/master/2021/io_netty/src/nio/NIODemo.java)
- [网络NIO例子](https://github.com/jnudeveloper/sharing/tree/master/2021/io_netty/src/nio/NetNIODemo.java)
- BIO提出的问题中，已解决的问题
    - 内存空间管理的封装问题: 封装数组的操作。（Buffer）
    - 输入输出操作的封装问题: 几乎每次操作都涉及到写和读，不如把两个操作封装到一个类里面。（Channel）
    - 阻塞问题: java没有直接的api来使得io变成非阻塞，因为IO复用已经从另一个角度解决了问题。
    - IO复用问题: 通过监听多个IO事件，使得阻塞的时间变短。（Selector）
- BIO提出的问题中，还剩下的问题
    - Buffer不可以动态拓展（类似数组），如果需要更大的空间只能出现分配并复制内容
    - 应用层协议和业务逻辑的封装、解耦问题
    - 线程管理还是要自己处理
- NIO新引入的问题
    - Buffer相关的API很难使用
    - 整个类库（buffer、channel、selector等）繁杂，使用难度大
    - 自己编码的可靠性问题与JDK的一些bug

### NIO2.0 JDK7 异步IO(AIO)
- [网络AIO例子](https://github.com/jnudeveloper/sharing/tree/master/2021/io_netty/src/aio/NetAIODemo.java)
- 问题: 我感觉AIO还是很清晰的，不知道为什么netty不使用AIO。可能是因为netty使用原生native代码的kqueue和epoll，性能已经很好了?

### 线程模型（IO线程的最佳实践）
- 上文我们一直关注IO，避开了解耦和线程管理的问题。
- 线程模型（自己想，看可以想到哪些）
    - 纯粹单线程: 我们之前的所有例子都是在一个线程里面运行的。
    - 一个请求一个线程: 主线程获取连接请求，对于每个请求，都新建一个线程来进行操作。（注意线程数不是越多越好）
    - 线程池: 主线程获取连接请求，对于每个请求，都提交到线程池进行操作。该线程池可以是固定大小的，也可以是动态拓展的，由开发者决定。
- reactor线程模型（dispatcher、acceptor、reactor、selector）
    - 单reactor单线程: 可能是1个线程、也可能分别1个线程（即2个线程）
    - 单Reactor多线程（线程池）
    - 主从Reactor线程（线程池）
    - 多Reactor线程（线程池）
- proactor线程模型: 异步相关，提交操作（函数指针）给操作系统，让操作系统完成。

### netty
- [netty代码](https://github.com/netty/netty) [netty官网](https://netty.io/)
- netty自己宣称的异步，其实是在应用层上的异步而不是IO模型里面的操作系统层的异步。
- 缺点: 又要再学多一套API，而且内容也不少。
- [具体例子](https://github.com/netty/netty/tree/4.1/example/src/main/java/io/netty/example)
- netty代码和线程模型的对应关系。
- 现在应该可以明白netty的基本思想和要解决的问题。而netty的源码，以后有机会再详细讲
- 也可以自己看书 [netty权威指南 李林锋著 电子版](https://github.com/many-books/study-book/blob/master/Netty%E6%9D%83%E5%A8%81%E6%8C%87%E5%8D%97%20PDF%E7%94%B5%E5%AD%90%E4%B9%A6%E4%B8%8B%E8%BD%BD%20%E5%B8%A6%E7%9B%AE%E5%BD%95%E4%B9%A6%E7%AD%BE%20%E5%AE%8C%E6%95%B4%E7%89%88.pdf)
- 记得要思考对应的类或包解决了什么问题，而不是陷入代码细节里面。

### 总结
最重要的2部分内容:
- IO模型、对应语言关于IO的API
- 线程模型、对应语言的关于多线程的API

我大概1年前和2个月前都看了netty的代码，现在写这篇文章的时候，我发现代码的具体内容我又忘记了，没忘的都是这些留下来的思想。
把握了上面这两个部分内容，即可根据不同的场景和技术，设计自己的io框架。
即使没有了netty，我们也可以写出高可用的网络通信代码，tomcat里面的通信部分就是一个不使用netty的例子，你们感兴趣可以自己去看。
