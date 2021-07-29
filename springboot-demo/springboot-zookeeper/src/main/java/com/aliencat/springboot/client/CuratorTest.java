package com.aliencat.springboot.client;

import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.framework.recipes.cache.NodeCache;
import org.apache.curator.framework.recipes.cache.PathChildrenCache;
import org.apache.curator.framework.recipes.cache.TreeCache;
import org.apache.curator.retry.ExponentialBackoffRetry;
import org.apache.zookeeper.CreateMode;
import org.apache.zookeeper.data.Stat;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.nio.charset.StandardCharsets;

public class CuratorTest {

    private CuratorFramework client;

    /**
     * 连接测试
     * 使用CuratorFrameworkFactory的两个静态工厂方法来创建zookeeper客户端对象。
     * 参数1：connectString，zookeeper服务器地址及端口号，多个zookeeper服务器地址以“,”分隔。
     * 参数2：sessionTimeoutMs，会话超时时间，单位毫秒，默认为60000ms。
     * 参数3：connectionTimeoutMs，连接超时时间，单位毫秒，默认为15000ms。
     * 参数4：retryPolicy，重试连接策略，有四种实现，分别为：
     * ExponentialBackoffRetry（重试指定的次数, 且每一次重试之间停顿的时间逐渐增加）、
     * RetryNtimes（指定最大重试次数的重试策略）、
     * RetryOneTimes（仅重试一次）、
     * RetryUntilElapsed（一直重试直到达到规定的时间）
     */
    @Before
    public void testConnect() {
        client = CuratorFrameworkFactory.builder()
                .connectString("192.168.124.18:2181")  //连接地址和端口号
                .sessionTimeoutMs(10000)     //会话超时时间
                .connectionTimeoutMs(1000)   // 连接超时时间
                .retryPolicy(new ExponentialBackoffRetry(1000, 10))  //重试策略
                .build();
        client.start();//开启连接
    }

    /**
     * 创建节点测试
     */
    @Test
    public void testCreateNode() throws Exception {
        //1.普通创建
        client.create().forPath("/test1");
        //2.创建多集节点
        client.create().creatingParentContainersIfNeeded().forPath("/test2/demo");
        //3.设置创建节点类型
        client.create().withMode(CreateMode.EPHEMERAL).forPath("/test3");
        //4.创建带指定数据的节点
        client.create().forPath("/test4", "This is test4".getBytes(StandardCharsets.UTF_8));
    }

    /**
     * 更新节点操作测试
     *
     * @throws Exception
     */
    @Test
    public void testUpdateNode() throws Exception {
        //指定版本更新节点数据
        client.setData().withVersion(0).forPath("/test4", "test4 has updated!".getBytes());
    }

    /**
     * 查询节点测试
     */
    @Test
    public void testQueryNode() throws Exception {
        Stat stat = new Stat();
        byte[] bytes = client.getData().storingStatIn(stat).forPath("/test4");
        System.out.println("路径/test4的数据是：" + new String(bytes));
        System.out.println("路径/test4的数据的版本是:" + stat.getVersion());
    }

    /**
     * 删除节点测试
     */
    @Test
    public void testDeleteNode() throws Exception {

        //只能删除叶子节点
        client.delete().forPath("/test1");
        //删除一个节点,并递归删除其所有子节点
        client.delete().deletingChildrenIfNeeded().forPath("/test2");
        //强制指定版本进行删除
        client.delete().withVersion(1).forPath("/test4");
        //注意:由于一些网络原因,上述的删除操作有可能失败,使用guaranteed()
        // 如果删除失败,会记录下来,只要会话有效,就会不断的重试,直到删除成功为止
        client.delete().guaranteed().forPath("/test3");
    }

    /**
     * NodeCache：监听节点的新增、修改操作
     *
     * @throws Exception
     */
    @Test
    public void testNodeCache() throws Exception {
        //最后一个参数表示是否进行压缩
        NodeCache cache = new NodeCache(client, "/super", false);
        cache.start(true);
        //只会监听节点的创建和修改，删除不会监听
        cache.getListenable().addListener(() -> {
            System.out.println("路径：" + cache.getCurrentData().getPath());
            System.out.println("数据：" + new String(cache.getCurrentData().getData()));
            System.out.println("状态：" + cache.getCurrentData().getStat());
        });

        client.create().forPath("/nodeCache", "1234".getBytes());
        Thread.sleep(1000);
        client.setData().forPath("/nodeCache", "5678".getBytes());
        Thread.sleep(1000);
        client.delete().forPath("/nodeCache");
        Thread.sleep(5000);
    }


    /**
     * PathChildrenCache：监听子节点的新增、修改、删除操作。
     *
     * @throws Exception
     */
    @Test
    public void testPathChildrenCache() throws Exception {
        //第三个参数表示是否接收节点数据内容
        PathChildrenCache childrenCache = new PathChildrenCache(client, "/super", true);
        /**
         * 如果不填写这个参数，则无法监听到子节点的数据更新
         如果参数为PathChildrenCache.StartMode.BUILD_INITIAL_CACHE，则会预先创建之前指定的/super节点
         如果参数为PathChildrenCache.StartMode.POST_INITIALIZED_EVENT，效果与BUILD_INITIAL_CACHE相同，只是不会预先创建/super节点
         参数为PathChildrenCache.StartMode.NORMAL时，与不填写参数是同样的效果，不会监听子节点的数据更新操作
         */
        childrenCache.start(PathChildrenCache.StartMode.POST_INITIALIZED_EVENT);
        childrenCache.getListenable().addListener((framework, event) -> {
            switch (event.getType()) {
                case CHILD_ADDED:
                    System.out.println("CHILD_ADDED，类型：" + event.getType() + "，路径：" + event.getData().getPath() + "，数据：" +
                            new String(event.getData().getData()) + "，状态：" + event.getData().getStat());
                    break;
                case CHILD_UPDATED:
                    System.out.println("CHILD_UPDATED，类型：" + event.getType() + "，路径：" + event.getData().getPath() + "，数据：" +
                            new String(event.getData().getData()) + "，状态：" + event.getData().getStat());
                    break;
                case CHILD_REMOVED:
                    System.out.println("CHILD_REMOVED，类型：" + event.getType() + "，路径：" + event.getData().getPath() + "，数据：" +
                            new String(event.getData().getData()) + "，状态：" + event.getData().getStat());
                    break;
                default:
                    break;
            }
        });

        client.create().forPath("/pathChildrenCache", "123".getBytes());
        client.create().creatingParentsIfNeeded().withMode(CreateMode.PERSISTENT).forPath("/super/c1", "c1内容".getBytes());
        //经测试，不会监听到本节点的数据变更，只会监听到指定节点下子节点数据的变更
        client.setData().forPath("/pathChildrenCache", "456".getBytes());
        client.setData().forPath("/pathChildrenCache/c1", "c1新内容".getBytes());
        client.delete().guaranteed().deletingChildrenIfNeeded().forPath("/super");
        Thread.sleep(5000);
    }

    /**
     * TreeCache：既可以监听节点的状态，又可以监听子节点的状态。
     *
     * @throws Exception
     */
    @Test
    public void testTreeCache() throws Exception {
        TreeCache treeCache = new TreeCache(client, "/treeCache");
        treeCache.start();
        treeCache.getListenable().addListener((curatorFramework, treeCacheEvent) -> {
            switch (treeCacheEvent.getType()) {
                case NODE_ADDED:
                    System.out.println("NODE_ADDED：路径：" + treeCacheEvent.getData().getPath() + "，数据：" + new String(treeCacheEvent.getData().getData())
                            + "，状态：" + treeCacheEvent.getData().getStat());
                    break;
                case NODE_UPDATED:
                    System.out.println("NODE_UPDATED：路径：" + treeCacheEvent.getData().getPath() + "，数据：" + new String(treeCacheEvent.getData().getData())
                            + "，状态：" + treeCacheEvent.getData().getStat());
                    break;
                case NODE_REMOVED:
                    System.out.println("NODE_REMOVED：路径：" + treeCacheEvent.getData().getPath() + "，数据：" + new String(treeCacheEvent.getData().getData())
                            + "，状态：" + treeCacheEvent.getData().getStat());
                    break;
                default:
                    break;
            }
        });

        client.create().forPath("/treeCache", "123".getBytes());
        client.create().creatingParentsIfNeeded().withMode(CreateMode.PERSISTENT).forPath("/treeCache/c1", "456".getBytes());
        client.setData().forPath("/treeCache", "789".getBytes());
        client.setData().forPath("/treeCache/c1", "910".getBytes());
        client.delete().forPath("/treeCache/c1");
        client.delete().forPath("/treeCache");
        Thread.sleep(5000);

    }

    /**
     * 关闭测试
     */
    @After
    public void testClose() {
        if (client != null) {
            client.close();
        }
    }
}
