package com.pattern.decorator;

/**
 * 具体的装饰动作
 */
public class DecoratorA extends Decorator {
		private String addState;
		
		
		public String getAddState() {
			return addState;
		}
		public void setAddState(String addState) {
			this.addState = addState;
		}
		
		//执行父类的操作
		@Override
		public void operation(){
			super.operation();
			System.out.println("添加了属性addStae,值为"+addState);
		}
		
		
}
