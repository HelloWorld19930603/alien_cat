## 基于windows 下的使用教程

### 1-下载安装rocket
https://www.apache.org/dyn/closer.cgi?path=rocketmq/4.9.0/rocketmq-all-4.9.0-bin-release.zip

### 2-配置环境变量
ROCKETMQ_HOME =(你的rocket解压位置) 

### 3-启动 mqnamesrv 服务
在rocket安装目录bin下执行 ***mqnamesrv.cmd*** 启动服务 
或者在dos窗口执行 ***.\mqnamesrv*** 命令

### 4-启动 broker 服务 
进入安装目录bin下执行以下命令：
.\mqbroker -n 127.0.0.1:9876 autoCreateTopicEnable=true

### 5-启动项目
运行RocketMqApplication