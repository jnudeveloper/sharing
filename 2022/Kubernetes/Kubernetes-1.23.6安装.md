# Installing Kubernetes v1.23.6 On Ubuntu20.04


###   一、 安装Docker
```
sudo apt-get update  

sudo apt install -y apt-transport-https ca-certificates curl software-properties-common  

curl -fsSL https://download.docker.com/linux/ubuntu/gpg | sudo apt-key add -  

sudo add-apt-repository "deb [arch=amd64] https://download.docker.com/linux/ubuntu bionic stable"    

sudo apt-get update  

apt-get install -y docker-ce=5:19.03.14~3-0~ubuntu-bionic

sudo mkdir -p /etc/docker

sudo tee /etc/docker/daemon.json <<-'EOF'
{
  "registry-mirrors": ["https://odp7qfll.mirror.aliyuncs.com"]
}
EOF

sudo systemctl daemon-reload	

sudo systemctl restart docker

```

###   二、 安装K8S

```
curl -fsSL https://mirrors.aliyun.com/kubernetes/apt/doc/apt-key.gpg | apt-key add -  

echo "deb https://mirrors.aliyun.com/kubernetes/apt kubernetes-xenial main" >> /etc/apt/sources.list  

apt update  

apt-get install -y kubelet=1.23.6-00 kubeadm=1.23.6-00 kubectl=1.23.6-00  


curl https://file.chuanyuezhiying.cn/pullk8s.sh | sh


#关闭swap
sed -ri 's/._swap._/#&/' /etc/fstab  

swapoff -a


```
###   三、 配置Master节点

```
hostnamectl set-hostname k8s-master  

kubeadm init --pod-network-cidr=10.244.0.0/16 --ignore-preflight-errors=NumCPU --image-repository registry.aliyuncs.com/google_containers  

echo "export KUBECONFIG=/etc/kubernetes/admin.conf" >> ~/.bash_profile 

source ~/.bash_profile



```

若初始化失败
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




#### 安装网络插件

```
kubectl apply -f https://raw.githubusercontent.com/coreos/flannel/master/Documentation/kube-flannel.yml  
```

#### 安装控制面板 
```
kubectl apply -f https://raw.githubusercontent.com/kubernetes/dashboard/v2.6.0/aio/deploy/recommended.yaml

kubectl proxy  

```
参考
https://github.com/kubernetes/dashboard



#### 配置用户 admin
```
wget https://file.chuanyuezhiying.cn/admin-sa.yaml  

kubectl apply -f ./admin-sa.yaml   

kubectl describe secret -n kube-system $(kubectl get secret -n=kube-system | grep admin-token- | awk {'print $1'})

```


###   四、 配置Slave节点及加入集群

完成docker及k8s安装后


```

#### master节点执行，获取加入语句
kubeadm token create --ttl 300s --print-join-command

hostnamectl set-hostname k8s-slave-00


kubeadm join 172.18.150.81:6443 --token pnos9z.wi43j7mqanlzki0l --discovery-token-ca-cert-hash sha256:6cd15be9319b8c3de11346f8d8007b1de0a875b53e55554205b7a7cd38619790 


```







