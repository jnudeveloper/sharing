# 压测工具JMeter

## 简介

ApacheJMeter™ 该应用程序是开源软件，是一个100%纯Java应用程序，旨在加载测试功能行为和测量性能。它最初设计用于测试Web应用程序，但后来扩展到其他测试功能。

Jmeter模拟用户的高并发请求，可以在短时间内对同个接口进行大量访问。

## 安装

安装Jmeter之前要先装好JDK，配置好Java的环境变量。

Jmeter 下载链接 https://jmeter.apache.org/download_jmeter.cgi

##### Windows

下载zip包，双击bin/jemter.bat打开

##### MAC

```shell
brew install jmeter
jmeter
```

## 基本使用

### 测试计划

1. 定义一些全局变量（详见BeanShell部分）
2. 如果勾选，则几个线程组是串行执行的（参考线程组部分）
3. 引入外部jar包（详见BeanShell部分）

![Image text](https://github.com/jnudeveloper/sharing/blob/master/2021/tuning/business_tuning/img/img1.png)

### 线程组

新建一个线程组。线程组可以类比线程池。用户设定好线程组的参数，jmeter给用户创建一个线程池。

![Image text](https://github.com/jnudeveloper/sharing/blob/master/2021/tuning/business_tuning/img/img2.png)

1.线程数量，模拟并发请求的机器数量。

2.要在多少秒内创建好上述线程。

3.每个线程执行多少次。一般是勾选前面的永远，然后看秒表计时，时间到了点停止。

![Image text](https://github.com/jnudeveloper/sharing/blob/master/2021/tuning/business_tuning/img/img3.png)

### 采样器

采样器是线程组要执行的内容

#### HTTP请求

新建一个HTTP请求

![Image text](https://github.com/jnudeveloper/sharing/blob/master/2021/tuning/business_tuning/img/img4.png)

类似postman。按需填写。

![Image text](https://github.com/jnudeveloper/sharing/blob/master/2021/tuning/business_tuning/img/img5.png)

上方的三个按钮：

1.运行

2.停止。停止所有线程。

3.关闭。线程会在当前运行任务结束后停止，不会终止正在执行的任务。

请求的是一个打印随机数的简单接口，代码如下。

```java
private Random seed = new Random(1);
@GetMapping("/getRandom")
public Integer getRamdon() {
    int a = seed.nextInt(100);
    log.info("随机数：{}", a);
    return a;
}
```

运行之后在服务端（我的idea的控制台）可以看到输出结果，在Jmeter界面则看不到输出。

## 图表

## 监听器

## 控制器

## 断言

## 输入可变参数

### CSV文件导入

### BeanShell

### CLI模式

由于Jmeter是一个纯Java的应用，用GUI模式运行压力测试时，对客户端的资源消耗是很大的，所以在进行正式的压测时一定要使用非GUI模式运行。

官方提醒
```aidl
Don't use GUI mode for load testing !, only for Test creation and Test debugging.
For load testing, use CLI Mode (was NON GUI):
   jmeter -n -t [jmx file] -l [results file] -e -o [Path to web report folder]
& increase Java Heap to meet your test requirements:
   Modify current env variable HEAP="-Xms1g -Xmx1g -XX:MaxMetaspaceSize=256m" in the jmeter batch file
Check : https://jmeter.apache.org/usermanual/best-practices.html
```

### 在CLI模式下如何停止测试计划

在bin目录下，运行脚本

1.stoptest.cmd / stoptest.sh 【硬中断】

2.shutdown.cmd / shutdown.sh 【软中断】

## 参考资料
https://www.cnblogs.com/poloyy/category/1746599.html?page=4

