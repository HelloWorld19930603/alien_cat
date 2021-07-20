package com.pattern.strategy.cashImpl;

import com.pattern.strategy.CashSuper;

/**
 * 打折算法
 *
 * @author Administrator
 */
public class CashRebatax implements CashSuper {

    private double rebatax;

    public CashRebatax(double rebatax) {
        // TODO Auto-generated constructor stub
        this.rebatax = rebatax;
    }

    @Override
    public double getAcceptRetrun(double money) {
        // TODO Auto-generated method stub
        return money * rebatax;
    }
}
