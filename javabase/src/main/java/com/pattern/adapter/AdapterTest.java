package com.pattern.adapter;


/**
 * 适配器模式
 *  将一个接口转换成客户希望成为的另一个类的接口，
 *  适配器将原本由于接口不兼容而不能一起工作的两个类可以一起工作。
 *
 * 1.背景：假如你有一个德国手机，一个国产手机， 现在德国充电使用的
 * 是110电压，国产的插头都是220v电压，
 * 现在手机充电的插头兼容不了两种电压的充电方式。但是你充电的时候
 * 又不想关心这个问题，你只想去拿着你充电头去充电。所以你需要去找个
 * 东西帮你去处理选择不同电压的插排。
 *
 * 1.条件，根据new出来的不同类型的对象，选择不同的实现类进行处理。
 * 2.这个是在类级别上的差别。
 *
 * 3.有两个不同的类，一个是德国标准的四个针脚的插头
 *
 */
public class AdapterTest {
  public static void main(String[] args) {
    //随便创建一个插头让适配器查找适合的方式进项充电
      //此模式在springMVC中的视图解析器中有用到
      //用于渲染不同的view
//      DBSocket socket = new DBSocketImpl();
      GBSocket socket = new GBSocketImpl();
      GJBZSocket gjbzSocket = new SocketAdapter(socket);
      gjbzSocket.charge();

  }
}
