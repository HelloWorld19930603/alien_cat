package com.pattern.strategy;

import com.pattern.strategy.cashImpl.CashNormal;
import com.pattern.strategy.cashImpl.CashRebatax;
import com.pattern.strategy.cashImpl.CashReturn;

/**
 * 对用户提供的活动方法
 * 可以根据用户选择的活动类型算出最后的结账金额
 * @author Administrator
 *
 */
public class CashContext {
	private CashSuper cashSuper;//活动工厂类 不同活动类型	
	public CashContext(String type) {
			// TODO Auto-generated constructor stub
			switch(type){
			
			case "打折":
				this.cashSuper=new CashRebatax(0.8);
				break;
			case "满减":
				this.cashSuper=new CashReturn(200, 50);
				break;
			default:
				this.cashSuper=new CashNormal();
				break;
			}
		}
	
	public double getResult(double money){
		return this.cashSuper.getAcceptRetrun(money);
	}
}
