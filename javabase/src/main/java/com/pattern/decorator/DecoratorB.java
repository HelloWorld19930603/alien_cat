package com.pattern.decorator;

public class DecoratorB extends Decorator {
		/**
		 * 在装饰模式中添加一个行为（方法）并执行
		 */
	
	
		
	@Override
	public void operation() {
		// TODO Auto-generated method stub
		super.operation();
		fly();
	}
	
	
	private void fly(){
		System.out.println("我学会了飞！");
	}
	
	
}
