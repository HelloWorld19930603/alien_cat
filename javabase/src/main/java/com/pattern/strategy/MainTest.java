package com.pattern.strategy;

/**
 * 策略模式
 * 策略模式让调用方法内部的改变不会影响到使用方法的客户。
 *
 *
 * 简单策略配合工厂模式
 * @author sunwhite
 *业务背景叙述：
 *		超市商场结账时打活动：
 *			1.打八折
 *			2.满300减100
 *			3.正常不打折
 *			4.考虑可扩展性和程序灵活性
 */
public class MainTest {

	public static void main(String[] args) {
		CashContext cashNormal=new CashContext("正常");
		double money=cashNormal.getResult(500);
		System.out.println("正常结账的结果为："+money);
		
		CashContext cashReturn=new CashContext("打折");
		 money=cashReturn.getResult(500);
		System.out.println("满减后的结果为："+money);
		
		CashContext cashRebatax=new CashContext("满减");
		 money=cashRebatax.getResult(500);
		System.out.println("满减后的结果为："+money);
	}
	/**
	 * 策略模式目的：
	 * 	1.对于用户来说：只需要使用一个类就可以使用不同的活动进行打折。
	 * 	2.对于开发者来说：
	 * 		1).提高程序的可扩展性。随意增减一个活动形式 比如：增加一个满100送纸巾 ，
	 * 		       满200送电脑活动，只需要实现CashSuper超类，输入消费金额返回不同的数值
	 * 		       对应不同的中奖商品即可，。
	 * 		2).提高程序的灵活性。比如，需要打不同折的商品，可以直接传入优惠打折到折扣活动
	 * 		        类即可，需要既打折又满减的活动，可以直接执行两个工厂的活动。
	 * 	3.设计模式的最终目的：
	 * 		1).提高代码的可重用性。一般通过抽方法。每个方法执行自己这一类的职能。这个方法的
	 * 		       核心在于 降低方法或者类之间的耦合度。
	 *      2).提高程序性的灵活性和可扩展性。这个比较难需要慢慢体会。一般通过继承和多态实现。
	 *         
	 */


}
