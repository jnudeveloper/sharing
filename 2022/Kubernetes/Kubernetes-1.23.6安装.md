# Installing Kubernetes v1.23.6 On Ubuntu20.04


###   一、 安装Docker
```
//更新
sudo apt-get update  

//安装依赖
sudo apt install -y apt-transport-https ca-certificates curl software-properties-common  

//添加apt-key
curl -fsSL https://download.docker.com/linux/ubuntu/gpg | sudo apt-key add -  

//添加amd64的docker安装资源
sudo add-apt-repository "deb [arch=amd64] https://download.docker.com/linux/ubuntu bionic stable"    

//更新
sudo apt-get update  

//安装docker-ce v19.03.14
apt-get install -y docker-ce=5:19.03.14~3-0~ubuntu-bionic

//创建Docker配置文件夹
sudo mkdir -p /etc/docker

//设置加速路径
sudo tee /etc/docker/daemon.json <<-'EOF'
{
  "registry-mirrors": ["https://odp7qfll.mirror.aliyuncs.com"]
}
EOF

//重启docker
sudo systemctl daemon-reload	

sudo systemctl restart docker

```

###   二、 安装K8S

```
//添加apt-key
curl -fsSL https://mirrors.aliyun.com/kubernetes/apt/doc/apt-key.gpg | apt-key add -  

//添加k8s资源
echo "deb https://mirrors.aliyun.com/kubernetes/apt kubernetes-xenial main" >> /etc/apt/sources.list  

//更新
apt update  

//安装k8s三🗡客，版本1.23.6
apt-get install -y kubelet=1.23.6-00 kubeadm=1.23.6-00 kubectl=1.23.6-00  

//通过docker来拉取k8s启动所需镜像（国内防火墙限制，很慢，故走docker途径）
curl https://file.chuanyuezhiying.cn/pullk8s.sh | sh


#关闭swap
sed -ri 's/._swap._/#&/' /etc/fstab  

swapoff -a

```

###   二点半、设置docker的

##### 若k8s与docker的cgroupdriver不一致，则需要修改docker.service的启动参数，将cgroupdriver改为systemd，不然kubelet无法启动

```
sed -i 's#--containerd=/run/containerd/containerd.sock#--containerd=/run/containerd/containerd.sock --exec-opt native.cgroupdriver=systemd#g' /usr/lib/systemd/system/docker.service  

rm -rf /etc/kubernetes/manifests    
systemctl daemon-reload    
systemctl restart docker  
echo "y" | kubeadm reset  
systemctl stop kubelet  
rm -rf /var/lib/cni/  
rm -rf /var/lib/kubelet/*  
rm -rf /etc/cni/  
ifconfig cni0 down  
ifconfig flannel.1 down  
ip link delete cni0  
ip link delete flannel.1  
systemctl daemon-reload  
service docker restart  
service kubelet start  

```

###   三、 配置Master节点

#### 1、初始化master节点

```
//设置计算机名
hostnamectl set-hostname k8s-master  

//初始化master节点（init指令只用于master节点）
kubeadm init --pod-network-cidr=10.244.0.0/16 --ignore-preflight-errors=NumCPU --image-repository registry.aliyuncs.com/google_containers  

//设置环境变量
echo "export KUBECONFIG=/etc/kubernetes/admin.conf" >> ~/.bash_profile 

source ~/.bash_profile


```


#### 2、安装网络插件

```
kubectl apply -f https://raw.githubusercontent.com/coreos/flannel/master/Documentation/kube-flannel.yml  
```

#### 3、安装控制面板 
```
kubectl apply -f https://raw.githubusercontent.com/kubernetes/dashboard/v2.6.0/aio/deploy/recommended.yaml

kubectl proxy  

```


访问：
https://localhost:8001/api/v1/namespaces/kubernetes-dashboard/services/https:kubernetes-dashboard:/proxy/#/login

#### 演示：
地址： https://k8s.test.amatech.cn/api/v1/namespaces/kubernetes-dashboard/services/https:kubernetes-dashboard:/proxy/#/login

token：
```
eyJhbGciOiJSUzI1NiIsImtpZCI6ImxTY1ZsazFpMTJOQkNVTEJ6Y1h0dnBkSFBjajBRaWQ0VlZnUFYxaEpvYTQifQ.eyJpc3MiOiJrdWJlcm5ldGVzL3NlcnZpY2VhY2NvdW50Iiwia3ViZXJuZXRlcy5pby9zZXJ2aWNlYWNjb3VudC9uYW1lc3BhY2UiOiJrdWJlLXN5c3RlbSIsImt1YmVybmV0ZXMuaW8vc2VydmljZWFjY291bnQvc2VjcmV0Lm5hbWUiOiJhZG1pbi10b2tlbi1yc3QyNCIsImt1YmVybmV0ZXMuaW8vc2VydmljZWFjY291bnQvc2VydmljZS1hY2NvdW50Lm5hbWUiOiJhZG1pbiIsImt1YmVybmV0ZXMuaW8vc2VydmljZWFjY291bnQvc2VydmljZS1hY2NvdW50LnVpZCI6IjFlOTRjNTI3LWJjMjItNDJhNC1hZDRlLTRlM2YzN2Y0Yzk3OSIsInN1YiI6InN5c3RlbTpzZXJ2aWNlYWNjb3VudDprdWJlLXN5c3RlbTphZG1pbiJ9.XuO2TVLaBM9bkgM1qdlESOtvs5KzfQyrNYEHIPhgGDYCWOTVMo61pEwUPYkc6-Hj1olTUiURBC1KPcKA_mzc8hGLx_jWFerQGf8ZaabYDV_csky8NozFj2TFdyp23f67l0FSdTI80HTfIE1fIasXcxfyVBxWTcUh_wKZK9eItInz5sfnhOywWUOdE8BfhQyybqpDA9eEHInbuWzEXD9K02mGQHkXOpxlMZ1tJFEZtQopFvA2bPOgdMaBFebT4PBKnn98z9Bd2vfP_gsHR5ZwZrZJ5ExF7wVtU5l7lHENbmid6UStAOtQLCh9V1zprDCf5CYM0Wop2acWeiJo_w__BQ
```

#### 4、配置用户 admin

```
//下载配置admin账号yaml
wget https://file.chuanyuezhiying.cn/admin-sa.yaml  

//执行yaml以创建账号
kubectl apply -f ./admin-sa.yaml   

//获取admin账号的token，用于kubernetes-dashboard登录
kubectl describe secret -n kube-system $(kubectl get secret -n=kube-system | grep admin-token- | awk {'print $1'})

```


###   四、 配置Slave节点及加入集群

###### 完成docker及k8s安装，以及修改docker cgroupdriver 后方可加入


```

#### master节点执行，获取加入语句
kubeadm token create --ttl 300s --print-join-command

//设置节点计算机名
hostnamectl set-hostname k8s-slave-00

//加入集群
kubeadm join 172.18.150.81:6443 --token pnos9z.wi43j7mqanlzki0l --discovery-token-ca-cert-hash sha256:6cd15be9319b8c3de11346f8d8007b1de0a875b53e55554205b7a7cd38619790 

```

###### 实际应用中，我们会创建一个以完成环境安装OS镜像，配合初始化脚本，可以实现节点自动加入集群的效果。


###   五、 参考

kubernetes.io : https://kubernetes.io/docs/setup/

kubernetes-dashboard : https://github.com/kubernetes/dashboard












