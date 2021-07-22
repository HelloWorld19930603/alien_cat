package com.pattern.decorator;

/**
 * 装饰者（粘合剂）
 * 作用：将装饰者主题和装饰者类联系到一块
 * 使用：可以是一个或多个接口方法。方法就是进行装饰需要进行装饰的方法。
 * @author Administrator
 *
 */
public interface Component {

		public void operation();
}
