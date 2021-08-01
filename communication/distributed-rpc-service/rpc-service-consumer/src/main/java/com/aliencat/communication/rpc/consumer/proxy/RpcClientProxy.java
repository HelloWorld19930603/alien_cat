package com.aliencat.communication.rpc.consumer.proxy;

import com.alibaba.fastjson.JSON;
import com.aliencat.communication.rpc.common.RpcRequest;
import com.aliencat.communication.rpc.common.RpcResponse;
import com.aliencat.communication.rpc.consumer.client.RpcClient;
import com.aliencat.communication.rpc.consumer.zk.Host;
import com.aliencat.communication.rpc.consumer.zk.MyZkClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.UUID;

/**
 * 客户端代理类-创建代理对象
 * 1.封装request请求对象
 * 2.创建RpcClient对象
 * 3.发送消息
 * 4.返回结果
 */
@Component
public class RpcClientProxy {

    private static int index = 0;

    @Autowired
    private MyZkClient myZkClient;

    private RpcClient getClient() {
        if (myZkClient.getServerMap().size() == 0) {
            throw new RuntimeException("无可用服务器");
        }
        String bestServer = myZkClient.getBestServer();
//        Object[] objects = myZkClient.getNodeSet().toArray();
//        String node = objects[index % myZkClient.getServerMap().size()].toString();
        Host host = myZkClient.getServerMap().get(bestServer);
        RpcClient rpcClient = new RpcClient(host.getIp(), host.getPort());
        index++;
        return rpcClient;
    }

    public Object createProxy(Class serviceClass) {
        return Proxy.newProxyInstance(Thread.currentThread().getContextClassLoader(),
                new Class[]{serviceClass}, new InvocationHandler() {
                    @Override
                    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
                        //1.封装request请求对象
                        RpcRequest rpcRequest = new RpcRequest();
                        rpcRequest.setRequestId(UUID.randomUUID().toString());
                        rpcRequest.setClassName(method.getDeclaringClass().getName());
                        rpcRequest.setMethodName(method.getName());
                        rpcRequest.setParameterTypes(method.getParameterTypes());
                        rpcRequest.setParameters(args);
                        //2.创建RpcClient对象
                        RpcClient rpcClient = getClient();

                        try {
                            long startTime = System.currentTimeMillis();
                            //3.发送消息
                            Object responseMsg = rpcClient.send(JSON.toJSONString(rpcRequest));
                            RpcResponse rpcResponse = JSON.parseObject(responseMsg.toString(), RpcResponse.class);
                            if (rpcResponse.getError() != null) {
                                throw new RuntimeException(rpcResponse.getError());
                            }
                            //4.返回结果
                            Object result = rpcResponse.getResult();

                            long endTime = System.currentTimeMillis();
                            //上报请求时长
                            myZkClient.setResponseTimeToNodeData(rpcClient.getIp() + ":" + rpcClient.getPort(),
                                    endTime - startTime);

                            System.out.println(String.format("当前RPC-Client-Connect：%s:%d，结果：%s, 耗时：%d",
                                    rpcClient.getIp(), rpcClient.getPort(), result.toString(), (endTime - startTime)));
                            return JSON.parseObject(result.toString(), method.getReturnType());
                        } catch (Exception e) {
                            throw e;
                        } finally {
                            rpcClient.close();
                        }

                    }
                });
    }

}
