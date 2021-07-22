package com.pattern.decorator;

/**
 * 装饰者类（基础类）
 * 本类主要作用：放入装饰者主体，并执行装饰主题重写的方法。
 * 本类可以放所有子装饰者共用的方法或者需要重复使用的代码
 * @author Administrator
 *
 */
public class Decorator implements Component
{
		
	protected Component component;
	
	public void setComponent(Component component){
		this.component=component;
	}
	
	@Override
	public void operation(){
		
		if(this.component!=null){
			component.operation();
		}
		
	}

}
