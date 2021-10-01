## 案例分析（bug查看与分析、代码编写过程、补丁提交过程）
- 下面链接是方便自己查看和汇总。大部分补丁都在JDK主线上提交的，少部分是在仓库JDK16、JDK17上提交的，因为那时候在减速期1。
- [我的全部补丁](https://blog.prankdiary.com/2020/11/20/OpenJDK-Contribution-Summary/)
- [我在JDK主线上的补丁](https://github.com/openjdk/jdk/pulls?q=is%3Apr+author%3Algxbslgx)

### 第一个提交的补丁
- [bug链接](https://bugs.openjdk.java.net/browse/JDK-8254023)
- [PR详情](https://github.com/openjdk/jdk/pull/622)
- [补丁代码](https://github.com/openjdk/jdk/pull/622/files)

简单描述：就是一个没有`@Target`修饰的注解，是否可以用在模块上。也就是注解可以默认作用在哪些位置。代码例子可以看补丁代码的第3和第4个文件。按照JDK8-13的规范，是不可以作用在模块的。但是JDK14之后，规范变了，注解没有`@Target`也可以作用在模块了。也就是说，JDK14的规范改了，JDK14的java编译器实现没改。

但是因为对代码不熟悉，调试了2-3天，才找到这个位置（其他事都没干，只调这个bug）。期间怀疑的地方有3-4个，后来都排除了，最后找到了这个代码位置。
补丁其实很简单。看补丁的代码的第一个文件，有一个数组`dfltTargetMeta`，表示当没有`@Target`时，默认可以作用的地方。在那里加上`names.MODULE, names.TYPE_PARAMETER, names.TYPE_USE`等内容就行了。

提交补丁之后，其他开发者们对JDK14规范的这个修改又有了其他看法，认为默认注解不能作用在类型参数和类型使用上（也就是`names.TYPE_PARAMETER和names.TYPE_USE`），他们邮件交流一直很慢，导致我的补丁一直合并不了。

JDK16到了减速期1，他们发现再讨论这些，JDK16就没部分修复这个问题了，会影响很多用户。所以就直接先不管讨论的“类型参数和类型使用”（`names.TYPE_PARAMETER和names.TYPE_USE`）了，叫我先解决了模块的内容，所以我就把`names.TYPE_PARAMETER`和`names.TYPE_USE`去掉了，变成了现在你们在代码看到的样子，只加了` names.MODULE `。

后来又有另一个人说我的测试不够充分（原来的测试只有现在看到的第3和第4个文件）。测试只是表明了编译器运行通过，不能表示编译器运行过程中和运行之后的字节码中，注解真正存在。所以我加了后面的两个测试，一个是在注解处理的时候，检测注解存不存在。另一个是在产生字节码之后，检测字节码中，这个注解存不存在。

最后这个补丁就被合并了。但是后来发现，这个补丁是不完善的。具体情况就是，java有一个概念，叫“重复注解”，我的补丁不能解决重复注解的情况。后来有人提了[补丁](https://github.com/openjdk/jdk/pull/2412)，解决了这个问题，并且把两个位置合在一起，防止了像我这种之修改一半的情况。

再后来，他们的讨论终于有了结果：没有`@Target`的注解，可以作用在所所有“类型定义上下文”（规范9.6.4.1），不能作用在“类型使用上下文”（规范4.11）。
详见[JDK-8261610](https://bugs.openjdk.java.net/browse/JDK-8261610), [Fix the applicability of a no- at Target annotation type](https://mail.openjdk.java.net/pipermail/compiler-dev/2021-February/016321.html), [JDK-8270917](https://bugs.openjdk.java.net/browse/JDK-8270917),[JDK-8270916](https://bugs.openjdk.java.net/browse/JDK-82709160),[PR-256](https://github.com/openjdk/jdk17/pull/256)

虽然讨论有了结果，但是我昨天看的时候发现代码还是有问题。这次他们又是：只改了文档和代码注释，没有修改代码实现。也就是说，根据讨论的结果，我上面的`names.TYPE_USE`（类型使用）不加上去是对的，但是`names.TYPE_PARAMETER`是要加上去的，代码里面没加。所以我写了一个[评论](https://github.com/openjdk/jdk17/pull/256#issuecomment-902598483)，问他们是在JDK17改，还是JDK18改。感觉你们可以拿这个来熟悉代码，看有没有其他人想提补丁，没有提，我们找个人来提交补丁。

### 第一个合并的补丁
- [bug链接](https://bugs.openjdk.java.net/browse/JDK-8254557)
- [PR详情](https://github.com/openjdk/jdk/pull/718)
- [补丁代码](https://github.com/openjdk/jdk/pull/718/files)
- [我的总结博客](https://blog.prankdiary.com/2020/10/26/OpenJDK-8254557/)

虽然上一点说的是我的第一个补丁，但是它持续了几个月的时间，所以中间我也提交了很多其他的补丁。
这个补丁是我真正合并到主线的第一个补丁，所以我当时写了一篇总结，在上面的链接里面。

### 其他典型补丁
- 比较尴尬的例子 [bug链接](https://bugs.openjdk.java.net/browse/JDK-8216400) [PR详情](https://github.com/openjdk/jdk/pull/1895) 编译结束，需要关闭资源。我代码写错了，reviewer直接告诉我代码了。一般都只是描述，然后让别人来写的。很多bug，reviewer都知道怎么改，但是他们还是留着，为了吸引更多新人者进来。
- 比较自豪的例子 [bug链接](https://bugs.openjdk.java.net/browse/JDK-8236490) [PR详情](https://github.com/openjdk/jdk/pull/2060) 用户提交了一个Maven项目。太久时间没解决，提交bug的用户都找不到了。主要是数据溢出的问题。
- 测试相关的例子 [bug链接](https://bugs.openjdk.java.net/browse/JDK-8241187) [PR详情](https://github.com/openjdk/jdk/pull/1934) 一个反向查询的例子。测试代码可以改善的地方，也可以提PR，不过可能要等一段时间才有人review。
- 代码优化例子 [bug链接](https://bugs.openjdk.java.net/browse/JDK-8255729) [PR详情](https://github.com/openjdk/jdk/pull/1854) FilterOutputStream的write方法有性能问题，继承这个类时要重写这个方法。编译器的FilerOutputStream没有重写这个方法，导致性能慢了。PR时还有一个关于性能问题验证的讨论。

### 自己发现的bug或者增强(enhancement)
前面说的补丁都是我一个一个看bug列表找的、我能解决的bug，我后来也是这样逐步解决了很多bug。但是还有一些是我自己发现的问题，这里也说一下。

- 注释、文档相关的例子 [bug链接](https://bugs.openjdk.java.net/browse/JDK-8258525) [PR详情](https://github.com/openjdk/jdk/pull/1732) 主动积极提出问题，写补丁。
- 代码优化例子 [bug链接](https://bugs.openjdk.java.net/browse/JDK-8266675) [PR详情](https://github.com/openjdk/jdk/pull/3912) 工具类IntHashTable写得不好，不满足封装性和不易于使用。
- 代码重构例子 [bug链接](https://bugs.openjdk.java.net/browse/JDK-8265899) [PR详情](https://github.com/openjdk/jdk/pull/3673) JDK16实现了instanceof的模式匹配，我就在邮件列表征求意见，看要不要用新语法重构代码。整体代码还是清晰很多的。
- 无用代码例子 [bug链接](https://bugs.openjdk.java.net/browse/JDK-8267578) [PR详情](https://github.com/openjdk/jdk/pull/4157) 有些preview功能已经成为正式功能了，所以对preview的检查就可以删除了，但是有些残余代码还存在。我刚好看到，就把它删了。这个补丁来看，有些reviewer还是可能出错的。

个人使用java时，特殊情况较少，很难发现编译器的bug。所以我发现的都是增强问题，不算bug。

### bug解决关键要素（关键在第1、第2阶段）
- 构造重现例子：从用户或者提bug的人的例子里面提取出一个最简单的模型。
    - 不可重现的bug或者偶尔出现的bug，都比较难，目前我解决的都是可以重现的bug。
    - 一些maven和gradle项目就很考验提取代码的能力了。很多maven和gradle项目提的bug都是压了几个月或者几年都不修复，没法提取重现例子是最大的障碍。
- 代码定位：从报错提示出发，搜索代码，找到报错位置。然后逐步向上追寻错误数据的出处。
    - 善用IDE搜索功能、调试功能、代码跳转功能
    - 一些技巧要共享屏幕才好展示，之后看有没有工具分享我实际解决的过程。或者我录个屏幕视频。
- 尝试写补丁。不断尝试修改代码，构建jdk和测试问题是否解决。
- 写测试代码
    - 理论上测试代码是先写的，但是我每次都是先写好补丁，再写测试代码。
    - 主要是写测试代码用jtreg，有点不习惯，现在也没记住多少内容，每次都要翻之前的测试代码，参考着写。
- 测试代码的失败和通过
    - 写完测试，验证补丁的时候，会出现补丁或者测试代码有问题的情况。
    - 和平常的情况差不多。这时候补丁代码和测试代码都要看有没有错。

### 你们的第一个贡献（一些可能适合情况）
- 第一个补丁还是修typo或者文档比较好。易于熟悉环境和交流过程。
- 上文第一个提交的补丁最后的情况，可以提交一个补丁。
- 文档内容，比如`doc/building.md`里面的`hg clone...`，可以加上`git clone ...`等内容。
- javac的一个笔记

