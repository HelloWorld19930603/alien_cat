package com.aliencat.communication.rpc.provider.handler;

import com.alibaba.fastjson.JSON;
import com.aliencat.communication.rpc.common.RpcRequest;
import com.aliencat.communication.rpc.common.RpcResponse;
import com.aliencat.communication.rpc.provider.annotation.RpcService;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import org.springframework.beans.BeansException;
import org.springframework.cglib.reflect.FastClass;
import org.springframework.cglib.reflect.FastMethod;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

import java.lang.reflect.InvocationTargetException;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 服务端业务处理类
 * 1.将标有@RpcService注解的bean缓存
 * 2.接收客户端请求
 * 3.根据传递过来的beanName从缓存中查找到对应的bean
 * 4.解析请求中的方法名称. 参数类型 参数信息
 * 5.反射调用bean的方法
 * 6.给客户端进行响应
 */
@Component
@ChannelHandler.Sharable
public class RpcServerHandler extends SimpleChannelInboundHandler<String> implements ApplicationContextAware {

    private static final Map SERVICE_INSTANCE_MAP = new ConcurrentHashMap();

    /**
     * 1.将标有@RpcService注解的bean缓存
     *
     * @param applicationContext
     * @throws BeansException
     */
    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        Map<String, Object> serviceMap = applicationContext.getBeansWithAnnotation(RpcService.class);
        if (serviceMap != null && serviceMap.size() > 0) {
            Set<Map.Entry<String, Object>> entries = serviceMap.entrySet();
            for (Map.Entry<String, Object> item : entries) {
                Object serviceBean = item.getValue();
                if (serviceBean.getClass().getInterfaces().length == 0) {
                    throw new RuntimeException("服务必须实现接口");
                }
                //默认取第一个接口作为缓存bean的名称
                String name = serviceBean.getClass().getInterfaces()[0].getName();
                SERVICE_INSTANCE_MAP.put(name, serviceBean);
            }
        }
    }

    /**
     * 通道读取就绪事件
     *
     * @param channelHandlerContext
     * @param msg
     * @throws Exception
     */
    @Override
    protected void channelRead0(ChannelHandlerContext channelHandlerContext, String msg) throws Exception {
        //1.接收客户端请求- 将msg转化RpcRequest对象
        RpcRequest rpcRequest = JSON.parseObject(msg, RpcRequest.class);
        RpcResponse rpcResponse = new RpcResponse();
        rpcResponse.setRequestId(rpcRequest.getRequestId());
        try {
            //业务处理
            rpcResponse.setResult(handler(rpcRequest));
        } catch (Exception exception) {
            exception.printStackTrace();
            rpcResponse.setError(exception.getMessage());
        }
        //6.给客户端进行响应
        channelHandlerContext.writeAndFlush(JSON.toJSONString(rpcResponse));

    }

    /**
     * 业务处理逻辑
     *
     * @return
     */
    public Object handler(RpcRequest rpcRequest) throws InvocationTargetException {
        // 3.根据传递过来的beanName从缓存中查找到对应的bean
        Object serviceBean = SERVICE_INSTANCE_MAP.get(rpcRequest.getClassName());
        if (serviceBean == null) {
            throw new RuntimeException("根据beanName找不到服务,beanName:" + rpcRequest.getClassName());
        }
        //4.解析请求中的方法名称. 参数类型 参数信息
        Class<?> serviceBeanClass = serviceBean.getClass();
        String methodName = rpcRequest.getMethodName();
        Class<?>[] parameterTypes = rpcRequest.getParameterTypes();
        Object[] parameters = rpcRequest.getParameters();
        //5.反射调用bean的方法- CGLIB反射调用
        FastClass fastClass = FastClass.create(serviceBeanClass);
        FastMethod method = fastClass.getMethod(methodName, parameterTypes);
        return method.invoke(serviceBean, parameters);
    }


}
