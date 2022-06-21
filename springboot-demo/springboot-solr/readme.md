## 下载
从官方下载solr安装文件，solr下载地址：[Solr Downloads - Apache Solr](https://solr.apache.org/downloads.html)。
## 安装
下载文件之后，直接解压文件即可。
## 配置
windows环境下将solr安装目录下的bin路径配置到环境变量path中
## 启动
管理员身份打开命令行工具，进入bin目录，直接运行命令：
```
solr start -f
```
浏览器直接访问127.0.0.1:8983可以验证solr是否启动成功，同时可以进入页面进行solr的操作。

## 常用命令
### 查看信息
```
solr version
solr status
solr healthcheck [options]
```

### 启动和重启
```
solr start [options]
solr start -help
solr restart [options]
solr restart -help
```

### 停止
```
solr stop [options]
solr stop -help
```