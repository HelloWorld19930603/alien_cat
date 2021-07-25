package com.aliencat.springboot.controller;

import com.example.shopgoods.controller.zookeeper.zookeeperImpl.ZooKeeperImpl;
import org.apache.curator.framework.CuratorFramework;
import org.apache.zookeeper.data.Stat;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;


@RestController
@RequestMapping("/zookeeper")
public class ZooKeeperController {

    @Autowired
    private CuratorFramework zkClient;

    @Autowired
    private ZooKeeperImpl zooKeeper;


    /**
     * zookeeper 获取节点下的数据
     * <p>
     * post请求： http://localhost:8082/zookeeper/makeOrder?path=/task
     *
     * @param path
     * @return
     */
    @PostMapping("/getData")
    public String getData(@RequestParam String path) {
        byte[] bytes = null;
        try {
            bytes = zkClient.getData().forPath(path);
        } catch (Exception e) {
            e.printStackTrace();
        }

        String str = new String(bytes);
        return str;
    }


    @PostMapping("/create")
    public String create(@RequestParam String path) {
        try {
            zkClient.create().forPath(path);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "success";
    }


    @PostMapping("/delete")
    public String delete(@RequestParam String path) {
        try {
            zkClient.delete().forPath(path);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "success";
    }


    @PostMapping("/setData")
    public String setData(@RequestParam(value = "path") String path, @RequestParam(value = "data") String data) {
        try {
            zkClient.setData().forPath(path, data.getBytes());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "success";
    }


    @PostMapping("/check")
    public String check(@RequestParam(value = "path") String path) {
        Stat stat = null;
        try {
            stat = zkClient.checkExists().forPath(path);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "stat" + stat;
    }


    @PostMapping("/children")
    public String children(@RequestParam(value = "path") String path) {
        List<String> children = null;
        try {
            children = zkClient.getChildren().forPath(path);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "children" + children;
    }

    @PostMapping("/watch")
    public String watch(@RequestParam(value = "path") String path) {
        Stat stat = null;
        try {
            stat = zkClient.checkExists().watched().forPath(path);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "watch " + stat;
    }


    /**
     * zookeeper分布式锁
     *
     * @param product
     * @return
     */
    @PostMapping("/makeOrder")
    public String makeOrder(@RequestParam(value = "product") String product) {
        zooKeeper.makeOrder(product);
        return "success";
    }
}
