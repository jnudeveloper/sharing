# 压测工具JMeter

## 简介

ApacheJMeter™ 该应用程序是开源软件，是一个100%纯Java应用程序，旨在加载测试功能行为和测量性能。它最初设计用于测试Web应用程序，但后来扩展到其他测试功能。

模拟用户的高并发请求，可以在短时间内对同个接口进行大量访问。

## 安装

下载链接 https://jmeter.apache.org/download_jmeter.cgi

##### Windows

下载zip包，双击bin/jemter.bat打开

##### MAC

```shell
brew install jmeter
jmeter
```

## 基本使用

### 执行计划

1. 定义一些全局变量（详见BeanShell部分）
2. 如果勾选，则几个线程组是串行执行的（参考线程组部分）
3. 引入外部jar包（详见BeanShell部分）

![image-20211213230113961](C:\Users\Administrator.SKY-20181016MYJ\AppData\Roaming\Typora\typora-user-images\image-20211213230113961.png)

### 线程组

新建一个线程组。线程组可以类比线程池。用户设定好线程组的参数，jmeter给用户创建一个线程池。

![image-20211213222904969](C:\Users\Administrator.SKY-20181016MYJ\AppData\Roaming\Typora\typora-user-images\image-20211213222904969.png)

1.线程数量，模拟并发请求的机器数量。

2.要在多少秒内创建好上述线程。

3.每个线程执行多少次。一般是勾选前面的永远，然后看秒表计时，时间到了点停止。

![image-20211213232201169](C:\Users\Administrator.SKY-20181016MYJ\AppData\Roaming\Typora\typora-user-images\image-20211213232201169.png)

### 采样器

采样器是线程组要执行的内容

#### HTTP请求

新建一个HTTP请求

![image-20211213225027643](C:\Users\Administrator.SKY-20181016MYJ\AppData\Roaming\Typora\typora-user-images\image-20211213225027643.png)

类似postman。按需填写，填完后点击上方第一个绿色三角，运行

![image-20211213235804783](C:\Users\Administrator.SKY-20181016MYJ\AppData\Roaming\Typora\typora-user-images\image-20211213235804783.png)

请求的是一个打印随机数的简单接口

```java
private Random seed = new Random(1);
@GetMapping("/getRandom")
public Integer getRamdon() {
    int a = seed.nextInt(100);
    log.info("随机数：{}", a);
    return a;
}
```

运行之后在idea的控制台可以看到输出结果

![image-20211214000142145](C:\Users\Administrator.SKY-20181016MYJ\AppData\Roaming\Typora\typora-user-images\image-20211214000142145.png)

## 图表

## 监听器

## 控制器

## 断言

## 输入可变参数

### CSV文件导入

### BeanShell



