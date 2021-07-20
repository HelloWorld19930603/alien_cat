package com.pattern.template;

import com.pattern.template.exam.StudentDoAnswer;

/**
 * 1.模板模式背景
 *  1.老师出了一套试题,学生需要先把老师的试题抄下来然后作答，抄题过程麻烦而且易出错，
 *      所以需要把试题提取出来，让所有学生继承共用。同样做题行为除了答案不同，
 *      其他也是相同的，同样可以共用。
 *  2.
 *  2.不同的学生做题只需要填写答案即可
 * 2.模板模式解释
 *  1。将同一类对象（学生）的不同行为（做题）封装起来达到共用的目的
 *  2.利用java特性
 *      1.继承：子类继承父类做答案的方法
 *
 */
public class ExamMain {
  public static void main(String[] args) {
    // 小明对试题作答
    System.out.println("----------小明做试题----------");
      StudentDoAnswer xiaoming = new StudentDoAnswer();
      xiaoming.doQue1("2");
      xiaoming.doQue2("3");
      xiaoming.doQue3("3");
      System.out.println("----------小阳做试题----------");
      StudentDoAnswer xiaoyang = new StudentDoAnswer();
      xiaoyang.doQue1("6");
      xiaoyang.doQue2("7");
      xiaoyang.doQue3("8");
  }
}
