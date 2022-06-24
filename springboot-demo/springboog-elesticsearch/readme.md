## 下载ES
- elasticsearch的下载地址：https://www.elastic.co/cn/downloads/elasticsearch
- ik分词器的下载地址：https://github.com/medcl/elasticsearch-analysis-ik/releases
- kibana可视化工具下载地址：https://www.elastic.co/cn/downloads/kibana

> 注：由于es和jdk是一个强依赖的关系，所以当我们在新版本的ElasticSearch压缩包中包含有自带的jdk，但是当我们的Linux中已经安装了jdk之后，就会发现启动es的时候优先去找的是Linux中已经装好的jdk，此时如果jdk的版本不一致，就会造成jdk不能正常运行。

> 如果Linux服务本来没有配置jdk，则会直接使用es目录下默认的jdk，反而不会报错。
> 安装es8之前版本装需要JDK8及以上版本，安装es8版本则需要JDK17。


## ES安装及启动
1. 下载es后，完成解压。
2. 双击bin目录下的【elasticsearch.bat】即可启动es，默认启动后占用9200端口
3. 通过 http://127.0.0.1:9200/ 访问 
4. 安装ik分词器
在 es 目录中的 \plugins 目录下新建 ik 目录，然后将下载完成的 elasticsearch-analysis-ik 解压至此目录下。
完成后重启es，即可应用ik分词器。
5. 安装可视化工具kibana 下载解压kibana，可通过修改kibana中config目录下的***kibana.yml***文件，配置你的es地址：
即：elasticsearch.url: "http://127.0.0.1:9200"
6. 在kibana的bin目录下，双击kibana.bat，启动Kibana。通过浏览器访问 http://127.0.0.1:5601/app/kibana#/dev_tools/ 即可使用kibana访问es,进行后续的API测试。



## 使用docker安装ES
### 拉取镜像
docker pull elasticsearch:7.7.0

docker pull kibana:7.7.0

### 启动镜像
docker run --name elasticsearch -d -e ES_JAVA_OPTS="-Xms512m -Xmx512m" -e "discovery.type=single-node" -p 9200:9200 -p 9300:9300 elasticsearch:7.7.0

docker run -d -p 5601:5601 --name kibana --link elasticsearch:elasticsearch docker.io/kibana:7.7.0
### 访问
访问http:ip:9200 回车后得到一串es配置数据,则es配置成功

访问http://ip:5601 可以正常进入Kibana，说明kibana容器启动成功。


### 集群配置
#### 虚拟机的话先关防火墙
```
systemctl stop firewalld
systemctl disable firewalld
```

#### 设置内核参数：/etc/sysctl.conf
```
vm.max_map_count=655360   #sysctl -p 重启生效
```

#### 创建ES配置文件：
mkdir -p /data/elasticsearch/{data,logs,config,plugins}
chmod -R 777 /data/elasticsearch/data
chmod -R 777 /data/elasticsearch/logs
vi /data/elasticsearch/config/elasticsearch.yml

```
#跨域配置
http.cors.enabled: true
http.cors.allow-origin: "*"
#集群名称
cluster.name: elasticsearch-cluster
#节点名称
node.name: node-1
#是不是有资格竞选主节点
node.master: true
#是否存储数据
node.data: true
#最大集群节点数
node.max_local_storage_nodes: 3
#网络地址
network.host: 0.0.0.0
#设置其他节点与该节点交互的IP地址
network.publish_host: 192.168.3.90
#端口
http.port: 9200
#内部节点之间沟通端口
transport.tcp.port: 9300
#es7.x 之后新增的配置，写入候选主节点的设备地址，在开启服务后可以被选为主节点
discovery.seed_hosts: ["192.168.3.90","192.168.3.91","192.168.3.92"]
#es7.x 之后新增的配置，初始化一个新的集群时需要此配置来选举master
cluster.initial_master_nodes: ["node-1", "node-2","node-3"]
```

### 启动容器
```
docker run -d --name=elasticsearch   --restart=always   -p 9200:9200 -p 9300:9300   -v /data/elasticsearch/config/elasticsearch.yml:/usr/share/elasticsearch/config/elasticsearch.yml  -v /data/elasticsearch/data:/usr/share/elasticsearch/data   -v /data/elasticsearch/logs:/usr/share/elasticsearch/logs   -v /data/elasticsearch/plugins:/usr/share/elasticsearch/plugins  elasticsearch:7.7.0

#运行kibana 注意IP改成你自己的
docker run --name kibana -e ELASTICSEARCH_HOSTS=http://192.168.3.90:9200 -p 5601:5601 -d kibana:7.7.0
```
- -p 9200:9200端口映射：前表示主机部分，：后表示容器部分。 
- --name elasticsearch指定该容器名称，查看和进行操作都比较方便。 
- -v 挂载目录，规则与端口映射相同。 
- -d elasticsearch表示后台启动elasticsearch 
- plugins表示插件目录，logs表示日志，data表示节点数据


## 问题排查
### 查看集群健康
curl -XGET "http://$(hostname):9200/_cluster/health?pretty=true"
```
{
  "cluster_name" : "elasticsearch-cluster",
  "status" : "green",
  "timed_out" : false,
  "number_of_nodes" : 3,
  "number_of_data_nodes" : 3,
  "active_primary_shards" : 3,
  "active_shards" : 6,
  "relocating_shards" : 0,
  "initializing_shards" : 0,
  "unassigned_shards" : 0,
  "delayed_unassigned_shards" : 0,
  "number_of_pending_tasks" : 0,
  "number_of_in_flight_fetch" : 0,
  "task_max_waiting_in_queue_millis" : 0,
  "active_shards_percent_as_number" : 100.0
}
```
### 检查集群状态：
curl $(hostname):9200/_cluster/stats?pretty
curl http://$(hostname):9200/_cat/nodes?v&h=http,version,jdk,disk.total,disk.used,disk.avail,disk.used_percent,heap.current,heap.percent,heap.max,ram.current,ram.percent,ram.max,master

### 查看最新5分钟日志
```
docker logs --since 5m  a13e768edb4aed7c
```

### 如何修改es内存
修改jvm.options文件：


## 使用kibana操作es
### 创建索引
PUT elasticsearch_index
{
    "mappings": {
    "properties": {
    "name": {
        "type": "text"
        },
    "desc": {
        "type": "text"
        }
    }
    }
}
### 查看所有索引
GET _all/_settings

### 添加文档
PUT elasticsearch_index/_doc/1
{
    "name":"zhang san",
    "desc":"some things!"
}


### match_all全匹配查询
GET /elasticsearch_index/_search

### 统计总数据条数
GET /elasticsearch_index/_count

### 统计总存储空间占用
GET /_cat/shards?v

### 查看索引基本信息
GET /_cat/indices?v

### 测试默认分词器
GET _analyze			// ES引擎中已有standard分词器, 所以可以不指定index
{
"analyzer": "standard",
"text": "There-is & a DOG<br/> in house"
}

### 修改分词器
// 关闭索引:
POST elasticsearch_index/_close

// 启用English停用词token filter
PUT elasticsearch_index/_settings
{
"analysis": {
"analyzer": {
"my_token_filter": {        // 自定义的分词器名称
"type": "standard",
"stopwords": "_english_"
}
}
}
}

// 打开索引:
POST elasticsearch_index/_open

### 根据ID修改某一条数据的 某个字段 
PUT /elasticsearch_index/_doc/ID
{
    "name":"验证PUT方法"
}

### 根据ID删除某一条数据的 某个字段
DELETE /elasticsearch_index/_doc/ID

