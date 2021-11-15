# Docker 的设计思路及其应用

<img src="https://pic2.zhimg.com/v2-113d56db31a9e6d2bf7d8bd50e17babf_1440w.jpg?source=172ae18b" width="100%">

## 一、什么是 Docker？

Docker 是一个使用 Go 语言开发的开源的应用容器引擎，让开发者可以打包他们的应用以及依赖包到一个可移植的容器中，然后发布到任何流行的 Linux 或 Windows 操作系统的机器上，也可以实现虚拟化。容器是完全使用沙箱机制，相互之间不会有任何接口。

## 二、Docker 设计思路

### 一个完整的 Docker 有以下几个部分组成：

1. DockerClient 客户端  
   用户使用指令进行操作时于用户与 Daemon 间进行通信
2. Docker Daemon 守护进程  
   Docker 采用 C/S 架构 Docker daemon 作为服务端接受来自客户的请求，并处理这些请求（创建、运行、分发容器）。 客户端和服务端既可以运行在一个机器上，也可通过 socket 或者 RESTful API 来进行通信。
   Docker daemon 一般在宿主主机后台运行，等待接收来自客户端的消息。 Docker 客户端则为用户提供一系列可执行命令，用户用这些命令实现跟 Docker daemon 交互。
3. Docker Image 镜像  
   鲸鱼背上驮着的集装箱就是镜像，镜像本质就是文件，可以是我们运行环境的文件，也可以是运用程序的文件，docker 把它保存到本地。存储格式是以 Linux 的联合文件存储。Docker 镜像（Image）类似与虚拟机的镜像，可以将他理解为一个面向 Docker 引擎的只读模板，包含了文件系统。
   例如：一个镜像可以完全包含了 Ubuntu 操作系统环境，可以把它称作一个 Ubuntu 镜像。镜像也可以安装了 Apache 应用程序（或其他软件），可以把它称为一个 Apache 镜像。
4. DockerContainer 容器  
   Docker 容器通过 Docker 镜像来创建。容器与镜像的关系类似于面向对象编程中的对象与类。一个镜像，可以在同一服务器上部署成多个相同业务性质 Container，同时也可以在多台服务器上部署成多个 Container。

### 其核心设计思想：

1. 集装箱化  
   没有集装箱之前运输货物,东西零散容易丢失,有了集装箱之后货物不容易丢失,我们可以把货物想象成程序,目前我们要把程序部署到一台新的机器上,可能会启动不起来,比如少一些配置文件什么的或者少了什么数据,有了 docker 的集装箱可以保证我们的程序不管运行在哪不会缺东西。每个镜像就是一个集装箱。
2. 标准化

- 运输方式
  docker 运输东西有一个超级码头,任何地方需要货物都由 docker 鲸鱼先送到超级码头,然后再由 docker 鲸鱼从超级码头把货物送到目的地去.对应的技术来说,比如我们要把台式机的应用部署到笔记本上,我们可能选择用 QQ 发过去或者用 U 盘拷过去,docker 就标准化了这个过程,我们只需在台式机上执行一个 docker 命令,把鲸鱼派过来,把程序送到超级码头去,再在笔记本上执行一个 docker 命令,然后由鲸鱼把程序从超级码头送到笔记本上去。运输标准化 docker 标准化传输过程 执行 docker 命令就可完成发送传输

- 存储方式  
  把程序拷贝到笔记本上时,指定一个目录,我们还要记住这个目录,因为下次我们可能还要修改改动东西继续传,有了 docker 之后就不用，我们就不用记住了程序在哪里了,存储标准化了，我们想使用运行的时候只需要执行一条命令就行了。存储标准化 想使用运行的时候只需要执行一条命令

- API 接口
  ocker 提供了一系列 rest api 的接口,包含了对 docker 也就是对我们的应用的一个控制启动停止查看删除等等,如当我们要启动 tomcat 时我们要执行 startup 命令,当我们要停止时要执行 shutdown 命令,如果不是 tomcat,我们可能还需要一些别的命令控制它.有了接口标准化，只需要执行同样的命令，就能控制所有的应用，有了 docker，我们记 docker 的命令就可以对其进行操作.
  接口标准化 执行同样的命令控制所有的应用 记 docker 命令执行就好。

3. 隔离  
   我们在使用虚拟机时有自己的 cpu,硬盘,内存,完全感觉不到外面主机的存在,docker 也差不多,不过它更轻量,可以实现快速的创建和销毁，创建虚拟机可能要几分钟,创建 docker 只需要一秒.最底层的技术时 linux 一种内核的限制机制,叫做 LXC,LXC 是一种轻量级的容器虚拟化技术.最大效率的隔离了进程和资源.通过 cgroup,namespace 等限制,隔离进程组所使用的物理资源,比如 CPU,MEMORY 等等,这个机制在 7,8 年前已经加入到 linux 内核了,直到 2013 年 docker 出世的时候才火起来,大家可能奇怪为什么这么好的技术埋没这么多年都没人发现呢?其实不是这样的，docker 成功就像英雄造时势,时势造英雄,如果没有云计算,敏捷开发,高频度的弹性伸缩需求,没有 IT 行业这么多年长足的发展,也就没有 docker.
   每个 container 之间相互隔离，互不干扰。

### 镜像仓库

单机模式下，我们的镜像可能会在本机进行打包并调用 Docker Daemon API 推送到单台服务器上进行部署。但大多情况下，我们会需要一个 Docker Registry 来统一存放我们的镜像，当新服务器上的 Docker 需要部署新的 Container 时，直接从镜像仓库中拉取(pull)镜像并运行即可。  
镜像仓库:

- 官方公共仓库 Docker Hub(https://hub.docker.com/)
- 阿里云容器镜像服务(https://cr.console.aliyun.com/)(加速访问)
- 私人搭设的容器镜像仓库

### 三、Docker 解决了什么问题？

#### 系统环境不一致

场景：  
开发:我本地没问题啊！运维:服务器正常运行也没问题啦.

如果一个应用要正常的启动起来需要什么?比如 java web 应用.需要一个操作系统,操作系统之上要 jdk,tomcat,依赖于我们的代码,配置文件.操作系统的改变可能会导致我们的应用开不起来,比如我们调用了某些系统命令.jdk 版本也可能导致程序的运行失败.比如 class 文件需要 1.7 编译的,机器上装了个 1.6 的 jdk. 版本识别不了 tomcat 版本也能导致失败,比如旧的版本一些配置在新版本中不再支持.代码的话那就更有可能问题多多了，就比如应用了 C 盘,D 盘的一个文件,或者是用了系统的一些环境编码等等配置的话也是一样，少了某些配置文件，和系统相关的，就是跑不起来.下面 docker 来了,它把操作系统,jdk,tomcat,代码,配置都一个个全部放到鲸鱼上集装箱里.再打包放到鲸鱼上,由鲸鱼给我们送到服务器上,在我的机器上怎么运行,在别的机器上也怎么运行.不会有任何的问题.一句话就是 docker 解决了运行环境不一致所带来的问题.

#### 系统卡（资源抢占）

系统好卡,哪个哥们又写死循环了
如果大家有跟别人共用服务器的同学可能有这样的体会,莫名其妙发现自己的程序挂了,一查原因要不是内存不够了,要不是硬盘满了,还有就是发现某个服务变慢了,甚至敲终端都比较卡,但是 linux 本身就是一个多用户的操作系统本身就可以供多个用户人使用,docker 的隔离性可以解决这个问题,它是怎么解决的呢？如果放在 docker 上运行，就算别人的程序还是死循环疯狂吃 CPU,还是封装疯狂打日志，把硬盘占满, 还是内存泄漏,都不会导致我们的程序运行错误.因为 docker 在启动的时候就限定好了,它最大使用的 CPU 硬盘,如果超过了,就会杀掉对应进程.

#### 弹性扩容

双 11 来了,服务器撑不住了
大部分系统业务量并不是每天都比较平均的,特别是一些电商系统,每天总有那么几天业务量是平时的几倍甚至几十倍,如果按双 11 的规模去准备服务器那么对于平时的规模来说又是极大的浪费,所以就在节日前临时扩展机器,过完节再把多余的节点下线,这就给运维带来了非常大的工作量,一到过节就在各个机器上部署各种各样的服务,我们启动程序需要 java,tocmat 等等装好多好多东西,并且还可能起不来还要调试,这是非常恶心的工作,有了 docker 一切都变得美好了,只要点一下鼠标，服务器就可以从 10 台变成 100 台甚至 1000,1W 台.都是分分钟的事情.

### 四、Docker 如何安装？

Windows & Mac: https://www.docker.com/get-started

Linux-Ubuntu：
https://docs.docker.com/engine/install/ubuntu/

Linux-Centos:
https://docs.docker.com/engine/install/centos/

Linux-Debian:
https://docs.docker.com/engine/install/debian/

### 五、Docker Client 基础指令介绍 (Linux)

1.展示镜像列表

```
docker images
docker image ls
docker images -a
```

2.将镜像保存为文件

```
docker save -o 文件名   镜像名
例：
docker save -o centos8.tar centos:latest
```

3.导入保存的镜像文件

```
docker load -i 文件名
例：
docker load -i centos8.tar
```

4.从镜像仓库中拉取或者更新指定镜像

```
docker pull [OPTIONS] NAME[:TAG|@DIGEST]
例：
docker pull centos  # 拉取centos镜像，不填写版本号默认拉取latest
```

5.删除镜像，同时删除多个镜像时镜像名称或 id 用空格分隔

```
docker rmi [OPTIONS] IMAGE [IMAGE...]
例：
docker rmi centos:latest

```

6.标记本地镜像，将其归入某一仓库(创建一个新的 tag，新的名字新的版本号，但内容一致)

```
 docker tag SOURCE_IMAGE[:TAG] TARGET_IMAGE[:TAG]
 例：
 docker tag centos:latest  miao/centos-demo:1.0
```

7.将镜像推送到镜像仓库

```
docker push [OPTIONS] NAME[:TAG]
docker push  myimages:0.1
```

8.docker build 使用 Dockerfile 创建镜像(这里不具体介绍 Dockerfile)

```
命令格式如下，选项很多，可以通过 docker build --help 查看
docker build [OPTIONS] PATH | URL | -

#使用当前目录的 Dockerfile 创建镜像，Tag为 demo/docker:1.0
docker build -t demo/docker:1.0 .
```

9.创建容器(不启动)

```

docker create [OPTIONS] IMAGE [COMMAND] [ARG...]

#使用 centos:latest创建一个名称为 test 的容器
docker create  --name test centos:latest

```

10.通过镜像创建容器并启动

```
docker run [OPTIONS] IMAGE [COMMAND] [ARG...]

#使用 centos:latest创建一个名称为 test 的容器并运行
 docker run  --name test hello-world:latest
```

11.启动一个或多个已经停止的容器

```
docker start [ContainerId...]
docker start cea968a92c01
```

12.停止正在运行的容器

```
docker stop [ContainerId...]
docker stop cea968a92c01
```

13.重启容器

```
docker restart  [ContainerId...]
docker restart  cea968a92c01
```

14.杀死正在运行的容器

```
docker kill  [ContainerId...]
docker kill  cea968a92c01
```

14.删除已停止运行的容器

```
docker rm  [ContainerId...]
docker rm  cea968a92c01
```

14.列出当前容器

```
列出当前运行中的容器：
docker ps
列出当前所有容器：
docker ps -a
```

15.暂停/恢复容器中所有进程

```
# 暂停id为 cea968a92c01 的容器中所有进程
docker pause cea968a92c01

# 恢复id为 cea968a92c01 的容器中所有进程
docker unpause cea968a92c01
```

15.在容器中执行命令

```

# 命令格式
 docker exec [OPTIONS] CONTAINER COMMAND [ARG...]

 # 在id为 66c6f21aa2d1 的容器中运行/bin/bash 程序
 docker  exec -it 66c6f21aa2d1   /bin/bash
```

容器的 Create 及 Run 涉及参数较多，新学者建议对相关参数做一定的了解，推荐参考链接
https://www.runoob.com/docker/docker-run-command.html

### 六、Dockerfile 文件关键字介绍

Dockerfile 是一个用来构建镜像的文本文件，文本内容包含了一条条构建镜像所需的指令和说明。
简单例子：

```
#基础镜像
FROM java:8-jre

#SpringBoot项目必须使用/tmp目录
VOLUME /tmp

#将项目的jar包拷贝并命名
COPY target/*.jar running.jar

#暴露的端口
EXPOSE 8080

#执行命令运行项目
ENTRYPOINT ["java","-jar","running.jar"]

#解决时间不正确的问题
RUN /bin/cp /usr/share/zoneinfo/Asia/Shanghai /etc/localtime \
  && echo 'Asia/Shanghai' >/etc/timezone \

```

#### 1.FROM

定制的镜像都是基于 FROM 的镜像，这里的 java:8-jre 就是定制需要的基础镜像。实际上是继承了一个安装有 java8 环境的 Linux

```
FROM java:8-jre
```

#### 2.RUN

用于执行后面跟着的命令行命令。有以下俩种格式：  
RUN <命令行命令>

```
RUN <命令行命令>
# <命令行命令> 等同于，在终端操作的 shell 命令。
如打印文件列表
RUN ls /home
```

exec 格式：

```
RUN ["可执行文件", "参数1", "参数2"]
# 例如：
# RUN ["./test.php", "dev", "offline"] 等价于 RUN ./test.php dev offline
```

#### 3.COPY

复制指令，从上下文目录中复制文件或者目录（需要构建的目录，如源码目录）到容器里指定路径。
COPY [--chown=<user>:<group>] <源路径 1>... <目标路径>

```
如：将项目目录target下的jar文件复制到容器指定目录中
COPY target/*.jar running.jar
```

#### 4.CMD

类似于 RUN 指令，用于运行程序，但二者运行的时间点不同:

CMD 在 docker run 时运行。
RUN 是在 docker build。
作用：为启动的容器指定默认要运行的程序，程序运行结束，容器也就结束。CMD 指令指定的程序可被 docker run 命令行参数中指定要运行的程序所覆盖。  
 注意：如果 Dockerfile 中如果存在多个 CMD 指令，仅最后一个生效。

#### 5.ENTRYPOINT

类似于 CMD 指令，但其不会被 docker run 的命令行参数指定的指令所覆盖，而且这些命令行参数会被当作参数送给 ENTRYPOINT 指令指定的程序。
但是, 如果运行 docker run 时使用了 --entrypoint 选项，将覆盖 CMD 指令指定的程序。  
 优点：在执行 docker run 的时候可以指定 ENTRYPOINT 运行所需的参数。  
 注意：如果 Dockerfile 中如果存在多个 ENTRYPOINT 指令，仅最后一个生效。

```
ENTRYPOINT ["<executeable>","<param1>","<param2>",...]
```

若 Dockerfile 文件为：

```
FROM nginx
ENTRYPOINT ["nginx", "-c"] # 定参
CMD ["/etc/nginx/nginx.conf"] # 变参
```

不传参运行

```
docker run  nginx:test
容器程序默认启动指令:
nginx -c /etc/nginx/nginx.conf

```

传参运行

```
docker run  nginx:test -c /etc/nginx/new.conf

容器程序启动指令：
nginx -c /etc/nginx/new.conf

```

#### 6.ENV

设置环境变量，定义了环境变量，那么在后续的指令中，就可以使用这个环境变量。

```
如定义PORT为8081
ENV PORT 8081
ENTRYPOINT java -Dserver.port=$PORT -cp app:app/lib/* com.example.Main
当容器启动时,JAVA程序启动端口为8081
================我割==================
同时我们可以在docker run时设置PORT的值,如:
docker run -it --rm -e PORT=8088 miaomiaomi:latest
这样启动时,java程序占用的端口就变成了8088

```

#### 7.ARG

构建参数，与 ENV 作用一致。不过作用域不一样。ARG 设置的环境变量仅对 Dockerfile 内有效，也就是说只有 docker build 的过程中有效，构建好的镜像内不存在此环境变量。

#### 8.VOLUME

定义匿名数据卷。在启动容器时忘记挂载数据卷，会自动挂载到匿名卷。
作用:

- 避免重要的数据，因容器重启而丢失，这是非常致命的。
- 避免容器不断变大。

```
VOLUME ["<路径1>", "<路径2>"...]
VOLUME <路径>

在启动容器 docker run 的时候，我们可以通过 -v 参数修改挂载点。

```

#### 9.EXPOSE

声明映射端口

#### 10.WORKDIR

指定工作目录。用 WORKDIR 指定的工作目录，会在构建镜像的每一层中都存在。
（WORKDIR 指定的工作目录，必须是提前创建好的）。
docker build 构建镜像过程中的，每一个 RUN 命令都是新建的一层。只有通过 WORKDIR 创建的目录才会一直存在。

#### 11.USER

用于指定执行后续命令的用户和用户组，这边只是切换后续命令执行的用户（用户和用户组必须提前已经存在）。

#### 12.HEALTHCHECK

用于指定某个程序或者指令来监控 docker 容器服务的运行状态。

```
HEALTHCHECK [选项] CMD <命令>：设置检查容器健康状况的命令
HEALTHCHECK NONE：如果基础镜像有健康检查指令，使用这行可以屏蔽掉其健康检查指令
HEALTHCHECK [选项] CMD <命令> : 这边 CMD 后面跟随的命令使用，可以参考 CMD 的用法。
```

#### 13.ONBUILD

用于延迟构建命令的执行。简单的说，就是 Dockerfile 里用 ONBUILD 指定的命令，在本次构建镜像的过程中不会执行（假设镜像为 test-build）。当有新的 Dockerfile 使用了之前构建的镜像 FROM test-build ，这时执行新镜像的 Dockerfile 构建时候，会执行 test-build 的 Dockerfile 里的 ONBUILD 指定的命令。

#### 14.LABEL

LABEL 指令用来给镜像添加一些元数据（metadata），以键值对的形式，语法格式如下：

```
比如我们可以添加镜像的作者：
LABEL org.opencontainers.image.authors="runoob"
```

#### 15.ADD

ADD 指令和 COPY 的使用格类似（同样需求下，官方推荐使用 COPY）。  
功能也类似，不同之处如下：

- ADD 的优点：在执行 <源文件> 为 tar 压缩文件的话，压缩格式为 gzip, bzip2 以及 xz 的情况下，会自动复制并解压到 <目标路径>。
- ADD 的缺点：在不解压的前提下，无法复制 tar 压缩文件。会令镜像构建缓存失效，从而可能会令镜像构建变得比较缓慢。具体是否使用，可以根据是否需要自动解压来决定。

### 七、Docker 应用场景介绍

1. 简化配置
   这是 Docker 公司宣传的 Docker 的主要使用场景。虚拟机的最大好处是能在你的硬件设施上运行各种配置不一样的平台（软件、系统），Docker 在降低额外开销的情况下提供了同样的功能。它能让你将运行环境和配置放在代码中然后部署，同一个 Docker 的配置可以在不同的环境中使用，这样就降低了硬件要求和应用环境之间耦合度。
2. 代码流水线（Code Pipeline）管理  
   前一个场景对于管理代码的流水线起到了很大的帮助。代码从开发者的机器到最终在生产环境上的部署，需要经过很多的中间环境。而每一个中间环境都有自己微小的差别，Docker 给应用提供了一个从开发到上线均一致的环境，让代码的流水线变得简单不少。
3. 提高开发效率  
   这就带来了一些额外的好处：Docker 能提升开发者的开发效率。如果你想看一个详细一点的例子，可以参考 Aater 在 DevOpsDays Austin 2014 大会或者是 DockerCon 上的演讲。
   不同的开发环境中，我们都想把两件事做好。一是我们想让开发环境尽量贴近生产环境，二是我们想快速搭建开发环境。  
   理想状态中，要达到第一个目标，我们需要将每一个服务都跑在独立的虚拟机中以便监控生产环境中服务的运行状态。然而，我们却不想每次都需要网络连接，每次重新编译的时候远程连接上去特别麻烦。这就是 Docker 做的特别好的地方，开发环境的机器通常内存比较小，之前使用虚拟的时候，我们经常需要为开发环境的机器加内存，而现在 Docker 可以轻易的让几十个服务在 Docker 中跑起来。
4. 隔离应用  
   有很多种原因会让你选择在一个机器上运行不同的应用，比如之前提到的提高开发效率的场景等。  
   我们经常需要考虑两点，一是因为要降低成本而进行服务器整合，二是将一个整体式的应用拆分成松耦合的单个服务（译者注：微服务架构）。如果你想了解为什么松耦合的应用这么重要，请参考 Steve Yege 的这篇论文，文中将 Google 和亚马逊做了比较。
5. 整合服务器  
   正如通过虚拟机来整合多个应用，Docker 隔离应用的能力使得 Docker 可以整合多个服务器以降低成本。由于没有多个操作系统的内存占用，以及能在多个实例之间共享没有使用的内存，Docker 可以比虚拟机提供更好的服务器整合解决方案。
6. 调试能力  
   Docker 提供了很多的工具，这些工具不一定只是针对容器，但是却适用于容器。它们提供了很多的功能，包括可以为容器设置检查点、设置版本和查看两个容器之间的差别，这些特性可以帮助调试 Bug。你可以在《Docker 拯救世界》的文章中找到这一点的例证。
7. 多租户环境  
   另外一个 Docker 有意思的使用场景是在多租户的应用中，它可以避免关键应用的重写。我们一个特别的关于这个场景的例子是为 IoT（译者注：物联网）的应用开发一个快速、易用的多租户环境。这种多租户的基本代码非常复杂，很难处理，重新规划这样一个应用不但消耗时间，也浪费金钱。  
   使用 Docker，可以为每一个租户的应用层的多个实例创建隔离的环境，这不仅简单而且成本低廉，当然这一切得益于 Docker 环境的启动速度和其高效的 diff 命令。

8. 快速部署  
   在虚拟机之前，引入新的硬件资源需要消耗几天的时间。虚拟化技术（Virtualization）将这个时间缩短到了分钟级别。而 Docker 通过为进程仅仅创建一个容器而无需启动一个操作系统，再次将这个过程缩短到了秒级。这正是 Google 和 Facebook 都看重的特性。  
   你可以在数据中心创建销毁资源而无需担心重新启动带来的开销。通常数据中心的资源利用率只有 30%，通过使用 Docker 并进行有效的资源分配可以提高资源的利用率。

### 八、Docker 简单构建案例

参考代码
https://github.com/jnudeveloper/sharing/blob/master/2021/docker/src

### 九、持续化部署场景的应用(类似 Docker + Jenkins)

<img src="https://file.kuailejiequ.cn/view/7b696b980516496eb7fa1169d085a3b7" width="100%">


### 十、基础镜像选择
我们在构建属于我们自己镜像的时候，需要对基础镜像进行选型。比如，当我们需要选择一个Nodejs的基础镜像用来构建我们自己的Nodejs项目镜像，我们会访问链接https://hub.docker.com/，搜索node，会出现如下结果：
<img src="https://file.kuailejiequ.cn/view/ebfde44e054f4efdb0cf697de9e51a10" width="100%">
随后我们点击第一个item进入详情，就看见了让人眼花缭乱的版本及tag
<img src="https://file.kuailejiequ.cn/view/2fc1567f73bd4efe8ff48a0f80ac8c93" width="100%">
<img src="https://file.kuailejiequ.cn/view/e93937f2c6fa4cc2a6e5a5fa03fdbfa6" width="100%">
那我们该选哪个好呢？  
我们通过简单的分析，可以看到tag由为[版本号]+[系统版本]组成，如
17-alpine3.12，版本号为17，系统版本为alpine3.12，那么让我们简单介绍一下各个系统版本的区别：  
1.  Buster
buster是基于Debian Linux发行的一个版本，这个版本比较新，支持比较全面，受广大Debian爱好者的好评！所以像PHP、Python之类的语言、应用都会使用这个版本的Debian搭建Docker基础镜像。

2. Alpine
apline是Alpine Linux操作系统，它是一个独立发行版本，相比较Debian操作系统来说Alpine更加轻巧，而通过Docker镜像搭建微服务倡导的就是一个“轻量级”概念，所以很多语言、应用也都发布了Alpine版本的Docker基础镜像。

3. Stretch
stretch是Debian Linux发现的一个版本，这个版本在Debian Linux已经算是比较老旧的版本了，目前除了LTS其他版本已经不再提供技术支持了，所以我们非必要情况下还是不要选择它比较好。

根据实际情况选择相关的系统版本

### 十、参考链接

https://www.cnblogs.com/liuawen/p/12854029.html  
https://www.cnblogs.com/ay-a/p/13362774.html
https://www.runoob.com/docker/docker-dockerfile.html
