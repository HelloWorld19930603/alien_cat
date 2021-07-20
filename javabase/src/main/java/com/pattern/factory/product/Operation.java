package com.pattern.factory.product;

public class Operation {
	private double numberA;
	private double numberB;
	
	public double getNumberA() {
		return numberA;
	}

	public void setNumberA(double numberA) {
		this.numberA = numberA;
	}

	public double getNumberB() {
		return numberB;
	}

	public void setNumberB(double numberB) {
		this.numberB = numberB;
	}

	public double getResoule(double numberA,double numberB){//获取当前操作的结果
		return 0;
	}
}
