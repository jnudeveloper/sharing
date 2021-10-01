## 会议草稿

### 前期准备（没空可以不用管，到时候线上看Github代码就行，就是到时候怕网速慢）
- 下载JDK的代码，硬盘容量够的话从JDK11-17、JDK主线的代码都下载，不够的话，先下载JDK11或者JDK主线的就行。在github里面有`u-dev`结尾的优先下载，没有则优先下载`u`结尾的。再没有，则下载最普通的，比如JDK17,即没有jdk17u-dev也没有jdk17u，则下载[jdk17](https://github.com/openjdk/jdk17)。[jdk主线代码](https://github.com/openjdk/jdk) [jdk11u-dev](https://github.com/openjdk/jdk11u-dev)
- 有时间可以看下面内容，没时间就算了，明天我会一一说的。


### 我的经历（在家2年了）
- 第1年啥也不懂，啥都看，详细经历：https://zhuanlan.zhihu.com/p/345207940
- 第2年主要看OpenJDK的编译器前端Javac，最近成为OpenJDK社区的JDK项目的Committer（贡献到40个补丁的时候）。虽然成为Committer，但还是一个不太熟悉JDK代码的菜鸡，所以给JDK贡献代码是一个持久战。
- 个人后续安排：语言底层和应用层要同时发力。语言层面主要是对虚拟机（主要感兴趣是JIT编译器），应用层主要是通信框架netty和消息队列pulsar。
- 建议：不急，慢慢来，先看代码，不着急做贡献。积极交流，收获也很大。可以从库函数和设计模式开始学。提高主观能动性。


### OpenJDK社区（社区比代码重要）
- 这一部分我知乎开了个专栏，但是因为我懒，只有2篇文章，以后再更新吧。 [知乎专栏](https://www.zhihu.com/column/c_1336779862313566208)
- [OpenJDK官网](http://openjdk.java.net/) 左边导航栏有很多内容，简单介绍。睡前、上下班、平常无聊的时候看一眼即可，有个印象就行，我看了至少2篇也没记住多少。
- [社区角色](https://zhuanlan.zhihu.com/p/347754486) 简单介绍，具体的看链接。按照我的经验，可以争取1年半成为Committer，3年成为Reviewer。
- [邮件列表](https://zhuanlan.zhihu.com/p/367499011) 所有内容都会记录在邮件列表里面，包括Github的PR的内容，主要怕Github倒闭或者封禁。但是bug系统的评论不会记录，因为bug系统是oracle自己的。
- [bug系统](https://bugs.openjdk.java.net) 你们现在没有帐号，只能看bug，不能新建bug和评论。等你们贡献了2、3个补丁，就可以申请帐号了。
- [代码review系统](https://github.com/openjdk/jdk/pulls) 以前很复杂，JDK16之后就是Github的Pull Request。现在除了JDK8之外，其他的版本都在Github上面。
- [wiki](https://wiki.openjdk.java.net/) 一些额外的文档，内容较多，有空或者用到再看。
- 社区交流例子1：别人认领的任务你有解决方案，要征求认领人的意见。反面教材：[jdk/pull/4902](https://github.com/openjdk/jdk/pull/4902#pullrequestreview-714499097) 正面教材： [issue-8267610](https://bugs.openjdk.java.net/browse/JDK-8267610)
- 社区交流例子2：对别人的代码提出意见要委婉，尊重别人的思想和劳动成果，并且把采纳权交给对方（这也是我留在OpenJDK的原因）。正面教材：[jdk/pull/1490](https://github.com/openjdk/jdk/pull/1490#pullrequestreview-540865636) 反面教材：[pull/23811](https://github.com/spring-projects/spring-framework/pull/23811) [commit/dc59e50](https://github.com/spring-projects/spring-framework/commit/dc59e50561eeaedbc6f7a50d8703d627be2c6847)


### 代码开发分支
- 不是传统的Git分支管理（branch），而是直接开一个新的仓库（repository）。
- 现在社区主要是开发JDK18、17、11、8，有时候11、13、15也会有补丁，取决于开发者意愿。注意的是9、10、12、14基本不维护了。
- 代码发布流程，这里先简述，之后会写一个知乎文章。
- 用户角度：主线代码6个月一个大版本（每年3月、9月），每3年发布一个长期支持的大版本（11、17、23）。维护分支每3个月发布一个小版本。
- JDK开发者角度：[release process](http://openjdk.java.net/jeps/3) 主线6个月开发，3个月减速（修bug），然后发布大版本。维护分支，3个月开发，1个月减速（打tag，“观望”），然后发布小版本。


### OpenJDK代码目录
- 你对JDK的几乎所有疑问都可以在官方文档中找到。但是寻找要花时间与精力，如果有不会的，最好问我。我会的，就可以直接告诉你在哪里。我不知道，你再去找。
- [代码目录](http://openjdk.java.net/guide/#code-owners)也一样，在文档里有。但是你自己找，估计要找一会。
- jdk9之后给代码划分了模块，但是代码分在几个仓库里面，以HG（mercurial）的树林进行管理，提交记录混乱。jdk10之后把代码放在一个仓库里面，方便很多。jdk16之后，使用git，更方便了。
- 简单说一下各个目录，但是很多具体的内容可能我也不懂，因为整个JDK的内容实在太多了。


### JDK构建
- 最好使用linux，别用windows。
- 官方文档在doc/building.md里面，按照步骤来就行。其实就是一般make使用流程。获取代码`git clone`-检查本地配置生成配置文件`bash configure`-构建`make images`
- 每次构建都需要上一个版本或本版本的jdk，也就是说，需要先安装一个jdk11。然后使用按照的jdk11构建本地的jdk11u-dev,然后使用jdk11u-dev或按照的jdk11构建jdk12,使用jdk12构建jdk13,以此类推，耗时也较长。构建完成一次之后，后面就快了。
- 我也有一些笔记，在目录note/openjdk/debug里面。其中，build_and_test.md有你们这次要关注的构建内容。hotspot_debug.md是虚拟机调试的内容，就是调试c++代码。java_remote_debug.md是java调试远程调试的内容，jmh_build_and_run.md是性能测试的一些内容，你们现在暂时还没用到。


### 初学者的建议
- 师傅领进门，修行靠个人。提高主观能动性，我随时有空，你们有问题可以问我或者到邮件列表问。
- 不要害怕尝试，对于bug，有解决方案就可以提出来。和我或者社区交流，不要怕错。我之前也错了很多次，review我代码的人指出了我的错误，有时候还给了解决方案，我学会了知识，也得到了贡献“荣誉”，jdk还变得越来越好。双赢、三赢。
- 归根到底还是兴趣，找到自己感兴趣的jdk模块或者开源项目，然后支持做贡献，受益匪浅。
- 对市面上一些书的建议。

### 会议之后你们要做的事
- 下载OpenJDK的各个版本，构建下来，并且写个Helloworld，用新构建的版本运行。构建如果出现问题，随时沟通。
- 找到你们感兴趣的模块，开始看整体设计文档和代码。如果对Java编译器前端感兴趣，我知道的就更多一点，可以给的帮助更多一点。

### 之后可能要分享的内容
- jdk8-17的语法新功能。jdk这几年看起来好像加了很多内容，很多人觉得内容太多了，跟不上了，不如直接用jdk8就行了。其实是有些人把虚拟机、java语法、库函数的内容混在一起，以至于我们觉得越来越难。但是其实java语言改变的东西很少，每个点三两句话就可以解释完。而虚拟机的内容，一般应用层开发者不用关心（面试除外）。库函数用到再查就行了。居然有人出书来专门讲解java8、11的变化，我是不能理解的。所以我想花1个小时跟你们说完这些内容，避免看几百页的书。
- java的内存布局和虚拟机的一些知识，还没想好讲什么。你们也可以给些意见。讲这些也需要画图，感觉需要一些屏幕共享软件。
- 现在应用层的内容和生产环境的架构和应用，我都忘得差不多了，跟不上了。你们什么时候有兴趣或者有空也可以交流一下。

