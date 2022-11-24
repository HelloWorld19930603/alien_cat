# 使用 Docker 安装 Nebula Graph

### clone 仓库

1. [nebula-docker-compose 59](https://github.com/vesoft-inc/nebula-docker-compose)  nebula数据库的后端（我选用的是3.1.0版本）
2. [nebula-graph-studio 44](https://github.com/vesoft-inc/nebula-graph-studio)  nebula前端，master 分支（我选用的是3.4.2版本）

前后端版本对照表：

![\[外链图片转存失败,源站可能有防盗链机制,建议将图片保存下来直接上传(img-BwIxD7OZ-1666260182150)(C:\Users\Administrator\AppData\Roaming\Typora\typora-user-images\1666259569641.png)\]](https://img-blog.csdnimg.cn/53964d6b53034c378a75bc9e3b0e87a8.png)


### 安装并开启 nebula-docker-compose（后端）

- 来到 nebula-docker-compose 所在文件夹，按 shift 同时右键，选择打开 powershell

- 并输入命令 `docker-compose up -d` ，成功之后在 Docker 中会多出一些镜像

  ![\[外链图片转存失败,源站可能有防盗链机制,建议将图片保存下来直接上传(img-8MEfqJ6W-1666260182150)(C:\Users\Administrator\AppData\Roaming\Typora\typora-user-images\1666259756627.png)\]](https://img-blog.csdnimg.cn/80a39e741ca644c1a14825978e3978df.png)


![\[外链图片转存失败,源站可能有防盗链机制,建议将图片保存下来直接上传(img-DOMgynrZ-1666260182151)(C:\Users\Administrator\AppData\Roaming\Typora\typora-user-images\1666079866916.png)\]](https://img-blog.csdnimg.cn/f9b7d0acb8f34fbd9ee9807c58c1ff94.png)


- 上一步完成之后输入命令 `docker run --rm -ti --network nebula-docker-compose-310_nebula-net --entrypoint=/bin/sh vesoft/nebula-console:v3.0.0` （这里的`nebula-docker-compose-310_nebula-net`是我的nebula网络的名字，可以在命令行使用 `docker network ls` 查看网络）
  ![在这里插入图片描述](https://img-blog.csdnimg.cn/5898e766d1d745cda1934c6566d44502.png)



- 通过nebula-console客户端连接数据库：输入 ` nebula-console -u root -p nebula --address=graphd --port=9669` （graphd不用改），成功之后可输入 `show hosts` 检查 `nebula-storaged` 进程状态
- nebula数据库默认登录用户名和密码为：root/nebula。
  ![\[外链图片转存失败,源站可能有防盗链机制,建议将图片保存下来直接上传(img-J77G5RIJ-1666260182151)(C:\Users\Administrator\AppData\Roaming\Typora\typora-user-images\1666088262213.png)\]](https://img-blog.csdnimg.cn/e9a6b6e7bc7d48bcb6a5e0532f5eb351.png)
- 这样我们就可以直接使用命令行的形式去使用nebula数据库了。
- 退出的话请执行exit命令即可。

- nebula数据库 默认使用`9669`端口为客户端提供服务，如果需要修改端口，请修改目录`nebula-docker-compose`内的文件`docker-compose.yaml`，然后重启 nebula 服务


- 这时可以在命令行中来到 nebula-docker-compose 所在文件夹, 输入 `docker-compose ps` 列出 Nebula Graph 服务的状态和端口

![\[外链图片转存失败,源站可能有防盗链机制,建议将图片保存下来直接上传(img-Bfi9eAxz-1666260182151)(C:\Users\Administrator\AppData\Roaming\Typora\typora-user-images\1666083967547.png)\]](https://img-blog.csdnimg.cn/2f97407e45d64a6fa78b69018995878c.png)

- 此时后端算是成功部署并且开启了，停止服务可以使用 `docker-compose down`

### 安装并开启 nebula-graph-studio（前端）

- 来到 nebula-graph-studio 所在文件夹，同样在此打开 powershell，使用nodejs进行编译和运行前端项目
- **下载node.js:** https://nodejs.org/en/  (已安装的请跳过)
- 编译：npm install
- 运行：npm run dev-all

![\[外链图片转存失败,源站可能有防盗链机制,建议将图片保存下来直接上传(img-nUpWtCTl-1666260182151)(C:\Users\Administrator\AppData\Roaming\Typora\typora-user-images\1666086933066.png)\]](https://img-blog.csdnimg.cn/1b602af9bb0540e8b5a6b9cb937087ff.png)


- 启动成功后，在浏览器地址栏输入 `http://ip address:7001`，在浏览器窗口中能看到以下登录界面，表示已经成功部署并启动 Studio。
- 输入账号密码登录 Nebula Studio 后台（[http://127.0.0.1:7001/](http://127.0.0.1:7001)）


![\[外链图片转存失败,源站可能有防盗链机制,建议将图片保存下来直接上传(img-a8Pc4iHD-1666260182151)(C:\Users\Administrator\AppData\Roaming\Typora\typora-user-images\1666087327440.png)\]](https://img-blog.csdnimg.cn/6fbf17ab18e741389ccdb44677d8048a.png)

