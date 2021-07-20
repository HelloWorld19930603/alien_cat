package com.pattern.factory.factory;

import com.pattern.factory.product.Operation;
import com.pattern.factory.product.OperationAdd;
import com.pattern.factory.product.OperationSub;

public class OperationFactory {
	
	public static double getResult(double numberA,double numberB,String ope){
		
		try {
			Operation operation=null;
			switch(ope){
				case "+":
					operation=new OperationAdd();
					break;
				case "-":
					operation=new OperationSub();
					break;
				default:
					printError("输入操作符异常！");
			}	
			return operation.getResoule(numberA, numberB);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			printError("程序异常，请检查数据重新输入！");
			return 0;
		}
	}
	private static void printError(String string) {
		// TODO Auto-generated method stub
		System.out.println(string);
	}
}
