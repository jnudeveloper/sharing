# Kubernetes 入门级介绍

<img src="https://img.draveness.me/2020-05-06-15887760484503-kubernetes-banner.png" width="100%">

## 一、什么是 Kubernetes?

Kubernetes 是一个可移植、可扩展的开源平台，用于管理容器化的工作负载和服务，可促进声明式配置和自动化。 Kubernetes 拥有一个庞大且快速增长的生态系统，其服务、支持和工具的使用范围广泛。  
Kubernetes 这个名字源于希腊语，意为“舵手”或“飞行员”。k8s 这个缩写是因为 k 和 s 之间有八个字符的关系。 Google 在 2014 年开源了 Kubernetes 项目。Kubernetes 建立在 Google 大规模运行生产工作负载十几年经验的基础上，结合了社区中最优秀的想法和实践。

概述页面:
https://kubernetes.io/zh/docs/concepts/overview/what-is-kubernetes/

## 二、Kubernetes架构及设计思想
### 1、架构演变（机翻于官网）

文本来源：https://kubernetes.io/docs/concepts/overview/what-is-kubernetes/

<img src="https://d33wubrfki0l68.cloudfront.net/26a177ede4d7b032362289c6fccd448fc4a91174/eb693/images/docs/container_evolution.svg" width="100%" />

#### 传统部署时代： 
早期，组织在物理服务器上运行应用程序。无法为物理服务器中的应用程序定义资源边界，这会导致资源分配问题。例如，如果多个应用程序在物理服务器上运行，则可能存在一个应用程序会占用大部分资源的情况，因此其他应用程序的性能将不佳。一个解决方案是在不同的物理服务器上运行每个应用程序。但这并没有扩展，因为资源没有得到充分利用，而且组织维护许多物理服务器的成本很高。

####  虚拟化部署时代：

作为解决方案，引入了虚拟化。它允许您在单个物理服务器的 CPU 上运行多个虚拟机 (VM)。虚拟化允许应用程序在 VM 之间隔离，并提供一定程度的安全性，因为一个应用程序的信息不能被另一个应用程序自由访问。 

虚拟化允许更好地利用物理服务器中的资源并允许更好的可扩展性，因为可以轻松添加或更新应用程序，降低硬件成本等等。通过虚拟化，您可以将一组物理资源呈现为一次性虚拟机集群。  

每个 VM 都是在虚拟化硬件之上运行所有组件的完整机器，包括它自己的操作系统。  

#### 容器部署时代：
容器类似于虚拟机，但它们具有放松的隔离属性，可以在应用程序之间共享操作系统（OS）。因此，容器被认为是轻量级的。与 VM 类似，容器有自己的文件系统、CPU 份额、内存、进程空间等。由于它们与底层基础架构分离，因此它们可以跨云和操作系统分布移植。
容器之所以流行，是因为它们提供了额外的好处，例如：

敏捷的应用程序创建和部署：与使用 VM 映像相比，容器映像创建的简便性和效率更高。
持续开发、集成和部署：提供可靠且频繁的容器映像构建和部署以及快速高效的回滚（由于映像不变性）。

Dev 和 Ops 的关注点分离：在构建/发布时而不是部署时创建应用程序容器映像，从而将应用程序与基础架构解耦。
可观察性：不仅可以显示操作系统级别的信息和指标，还可以显示应用程序运行状况和其他信号。
开发、测试和生产之间的环境一致性：在笔记本电脑上运行与在云中运行相同。
云和操作系统分发可移植性：在 Ubuntu、RHEL、CoreOS、本地、主要公共云和其他任何地方运行。
以应用程序为中心的管理：将抽象级别从在虚拟硬件上运行操作系统提高到使用逻辑资源在操作系统上运行应用程序。
松散耦合、分布式、弹性、自由的微服务：应用程序被分解成更小的、独立的部分，并且可以动态部署和管理——而不是在一台大型单一用途机器上运行的单一堆栈。
资源隔离：可预测的应用程序性能。
资源利用：高效率、高密度。
### 2、为什么需要 Kubernetes 以及它可以做什么（机翻于官网）

文本来源：https://kubernetes.io/docs/concepts/overview/what-is-kubernetes/

容器是捆绑和运行应用程序的好方法。在生产环境中，您需要管理运行应用程序的容器并确保没有停机。例如，如果一个容器出现故障，则需要启动另一个容器。如果这种行为由系统处理，会不会更容易？

这就是 Kubernetes 来救援的方式！Kubernetes 为您提供了一个框架来弹性地运行分布式系统。它负责您的应用程序的扩展和故障转移，提供部署模式等等。例如，Kubernetes 可以轻松地为您的系统管理金丝雀部署。

Kubernetes 为您提供：

#### 服务发现和负载平衡 
Kubernetes 可以使用 DNS 名称或使用自己的 IP 地址公开容器。如果容器的流量很高，Kubernetes 能够负载均衡和分配网络流量，从而使部署稳定。  

####  存储编排 Kubernetes 
允许您自动挂载您选择的存储系统，例如本地存储、公共云提供商等。

自动推出和回滚 您可以使用 Kubernetes 描述已部署容器的所需状态，它可以以受控的速率将实际状态更改为所需状态。
例如，您可以自动化 Kubernetes 为您的部署创建新容器、删除现有容器并将其所有资源用于新容器。

自动装箱 你为 Kubernetes 提供了一个节点集群，它可以用来运行容器化的任务。你告诉 Kubernetes 每个容器需要多少 CPU 和内存 (RAM)。Kubernetes 可以将容器安装到您的节点上，以充分利用您的资源。

#### 自我修复 
Kubernetes 会重新启动失败的容器、替换容器、杀死不响应用户定义的健康检查的容器，并且在它们准备好服务之前不会将它们通告给客户端。


#### 秘密和配置管理 
Kubernetes 允许您存储和管理敏感信息，例如密码、OAuth 令牌和 SSH 密钥。您可以部署和更新机密和应用程序配置，而无需重新构建容器映像，也无需在堆栈配置中公开机密。

### 3、组件描述

<img src="https://ewr1.vultrobjects.com/imgur2/000/007/944/248_89d_0c3.png" width="100%"/>

<img src="https://d33wubrfki0l68.cloudfront.net/2475489eaf20163ec0f54ddc1d92aa8d4c87c96b/e7c81/images/docs/components-of-kubernetes.svg" width="100%"/>


参考：
https://kubernetes.io/zh-cn/docs/concepts/architecture/control-plane-node-communication/


## 三、Kubernetes场景演示

### 1、服务资源快速水平伸缩

### 2、服务资源自动水平伸缩
```
kubectl autoscale deployment test-go --min=2 --max=10
```
### 3、flannel网络服务，打通不同节点不同pod之间的通信

### 4、服务自动修复

### 5、服务快速版本切换

### 6、Node节点的增加


## 五、Kubernetes安装

https://github.com/jnudeveloper/sharing/blob/master/2022/Kubernetes/Kubernetes-1.23.6%E5%AE%89%E8%A3%85.md


## 六、常用指令

### 1、kubeadm

#### 主节点初始化
```
kubeadm init
```

#### 导出节点配置文件
```
kubeadm config print init-defaults > kubeadm-init-config.yaml
```


#### 加入集群
```
kubeadm join ${token}
```

### 2、kubectl

#### create 命令，用于创建Deployment和Service资源
```
kubectl create -f demo-deployment.yaml
kubectl create -f demo-service.yaml
``` 

#### delete 命令，用于删除Deployment和Service资源，也可以用来删除节点
```
kubectl delete -f demo-deployment.yaml
kubectl delete -f demo-service.yaml
kubectl delete node {Node Name}
....
``` 

#### get 命令，用于查看K8S集群的资源
```
//查看所有的资源信息
kubectl get all
kubectl get --all-namespaces

//查看pod列表
kubectl get pod

//显示pod节点的标签信息
kubectl get pod --show-labels

//根据指定标签匹配到具体的pod
kubectl get pods -l app=example

//查看node节点列表
kubectl get node 

//显示node节点的标签信息
kubectl get node --show-labels

//查看pod详细信息，也就是可以查看pod具体运行在哪个节点上（ip地址信息）
kubectl get pod -o wide

//查看服务的详细信息，显示了服务名称，类型，集群ip，端口，时间等信息
kubectl get svc
kubectl get svc -n kube-system

//查看命名空间
kubectl get ns
kubectl get namespaces

//查看所有pod所属的命名空间
kubectl get pod --all-namespaces

//查看所有pod所属的命名空间并且查看都在哪些节点上运行
kubectl get pod --all-namespaces  -o wide

//查看目前所有的replica set，显示了所有的pod的副本数，以及他们的可用数量以及状态等信息
kubectl get rs

//查看已经部署了的所有应用，可以看到容器，以及容器所用的镜像，标签等信息
kubectl get deploy -o wide
kubectl get deployments -o wide

``` 
更多请参考
https://blog.csdn.net/weixin_42408447/article/details/120999685


## 七、参考链接

http://www.javashuo.com/article/p-tesebkda-pv.html
https://kubernetes.io/docs/setup/
https://blog.csdn.net/weixin_42408447/article/details/120999685







