package com.aliencat.communication.rpc.provider.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 对外暴露服务接口
 */
@Target(ElementType.TYPE) // 用于接口和类上
@Retention(RetentionPolicy.RUNTIME)// 在运行时可以获取到
public @interface RpcService {
}
