package com.pattern.decorator;

/**
 * 装饰主题类
 * 被装饰者
 * @author Administrator
 *
 */
public class ComponentImpl implements Component {
	
	/**
	 * 实现Component接口，重写operation方法
	 * 装饰者类进行装饰时首先会执行此方法
	 */
	@Override
	public void operation() {
		// TODO Auto-generated method stub
			System.out.println("我是小明！");
	}

}
