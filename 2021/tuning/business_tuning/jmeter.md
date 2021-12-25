# 压测工具JMeter

## 简介

ApacheJMeter™ 该应用程序是开源软件，是一个100%纯Java应用程序，旨在加载测试功能行为和测量性能。它最初设计用于测试Web应用程序，但后来扩展到其他测试功能。

Jmeter模拟用户的高并发请求，可以在短时间内对同个接口进行大量访问。

这里只介绍了Jmeter的必要功能。Jmeter的特性还有很多，感兴趣的话可以点击最下方的参考资料链接查看。

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

## 监听器

### 查看结果树

这里可以看到每次请求的返回结果

![Image text](https://github.com/jnudeveloper/sharing/blob/master/2021/tuning/business_tuning/img/img6.png)

![Image text](https://github.com/jnudeveloper/sharing/blob/master/2021/tuning/business_tuning/img/img7.png)

### 聚合报告

生成一个统计表，列出线程组所有请求的响应时间数据。

![Image text](https://github.com/jnudeveloper/sharing/blob/master/2021/tuning/business_tuning/img/img8.png)

QPS = 并发线程数 / 响应时间平均值

QPS（每秒查询率）为服务器每秒能够相应的查询次数，是对一个特定的查询服务器在规定时间内所处理流量多少的衡量标准。

![Image text](https://github.com/jnudeveloper/sharing/blob/master/2021/tuning/business_tuning/img/img9.png)

## 配置原件

### HTTP信息头管理器

配置HTTP的请求头

![Image text](https://github.com/jnudeveloper/sharing/blob/master/2021/tuning/business_tuning/img/img10.png)

![Image text](https://github.com/jnudeveloper/sharing/blob/master/2021/tuning/business_tuning/img/img11.png)

## 输入可变参数

### CSV文件导入

提前生成数据，存放在CSV文件中。将CSV文件导入到Jmeter中。

![Image text](https://github.com/jnudeveloper/sharing/blob/master/2021/tuning/business_tuning/img/img13.png)

### BeanShell

可以使用Jmeter的内嵌代码来生成随机数据。

![Image text](https://github.com/jnudeveloper/sharing/blob/master/2021/tuning/business_tuning/img/img12.png)

BeanShell 是一种完全符合Java语法规范的脚本语言，并且又拥有自己的一些语法和方法。

下面是一段BeanShell代码。作用是自动生成并填充JSON数据的forder_id和order_sn变量值。

```java
import org.apache.commons.lang3.RandomStringUtils;
vars.put("forder_id", RandomStringUtils.randomAlphanumeric(50));
vars.put("order_sn", RandomStringUtils.randomAlphanumeric(50));

import java.util.Random;
vars.put("forder_id", "压测" + new Random().nextInt(Integer.MAX_VALUE) + new Random().nextInt(Integer.MAX_VALUE));
vars.put("order_sn", "压测" + new Random().nextInt(Integer.MAX_VALUE) + new Random().nextInt(Integer.MAX_VALUE));
```

对应的JSON数据为：

```json
{
  "order_id": "${forder_id}",
  "consignment_number": "veniam aute Duis enimexercitation",
  "order_sn": "${order_sn}",
  "sku_list": [
    {
      "sku_id": "consequat veniam fugiat id ininc",
      "sku_num_total": 3362795
    },
    {
      "sku_id": "test2 veniam fugiat id ininc",
      "sku_num_total": 3355795
    }
  ],
  "purchase_time": 4700007,
  "order_time": 43964068,
  "last_tracking_no": "dolorcillumlabore in aliqua inincididunt adipisicing ex Duisveni"
}
```

![Image text](https://github.com/jnudeveloper/sharing/blob/master/2021/tuning/business_tuning/img/img6.png)

## CLI模式

由于Jmeter是一个纯Java的应用，用GUI模式运行压力测试时，对客户端的资源消耗是很大的，所以在进行正式的压测时一定要使用非GUI模式运行。

官方提醒
```text
Don't use GUI mode for load testing !, only for Test creation and Test debugging.
For load testing, use CLI Mode (was NON GUI):
   jmeter -n -t [jmx file] -l [results file] -e -o [Path to web report folder]
& increase Java Heap to meet your test requirements:
   Modify current env variable HEAP="-Xms1g -Xmx1g -XX:MaxMetaspaceSize=256m" in the jmeter batch file
Check : https://jmeter.apache.org/usermanual/best-practices.html
```

### CLI模式可选参数

| 字段 | 含义 |
| ------ | ------ |
| -n | 指定 JMeter 将在 cli 模式下运行 |
| -t | 包含测试计划的 jmx 文件名称 |
| -l | 记录测试结果的 jtl 文件名称 |
| -j | 记录 Jmeter 运行日志的文件名称 |
| -g | 输出报告文件（ .csv 文件） |
| -e | 生成 html 格式的测试报表 |
| -o | 生成测试报表的文件夹，该文件夹必须不存在或者为空 |

### 在CLI模式下如何停止测试计划

在bin目录下，运行脚本

1.stoptest.cmd / stoptest.sh 【硬中断】

2.shutdown.cmd / shutdown.sh 【软中断】

## 参考资料
https://www.cnblogs.com/poloyy/category/1746599.html?page=4

