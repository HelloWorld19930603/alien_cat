package com.pattern.strategy.cashImpl;

import com.pattern.strategy.CashSuper;

/**
 * 买满减算法 例满300减100
 * @author Administrator
 *
 */
public class CashReturn implements CashSuper {
		
	private double all;//满的总金额
	private double mul;//减的金额
	
	
	public CashReturn(double all,double mul) {
		// TODO Auto-generated constructor stub
		this.all=all;
		this.mul=mul;
	}
	
	@Override
	public double getAcceptRetrun(double money) {
		// TODO Auto-generated method stub
		if(all==0){
			try {
				throw new Exception("除数不能为0！");
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return money-money/all*mul;
	}

		

}
