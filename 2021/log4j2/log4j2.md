## log4j2漏洞还原

### 思路
```
                                  ||--rmi协议-->  远程获取一个类
web应用（使用了log4j2）--JNDI协议--> ||
                                  ||--jdap协议--> 远程获取一个类
```

### JNDI 使用
- 使用http/http_run.sh启动一个http服务
- 使用rmi/rmi_run.sh启动一个rmi服务 或者 使用ldap/ldap_run.sh启动一个ldap服务
- 使用jndi/jndi_run.sh启动应用

### 漏洞重现步骤
- 攻击者
  - 使用http/http_run.sh启动一个http服务
  - 使用rmi/rmi_run.sh启动一个rmi服务 或者 使用ldap/ldap_run.sh启动一个ldap服务
- 公司/组织的应用
  - 使用web-app/app_run.sh模拟一个web应用

### 解决方案
- 升级JDK的小版本
- 升级log4j2到2.15
- 一些jdk或log4j配置
- 网关直接过滤"${jndi:ldap}"和"${jndi:rmi:}"
