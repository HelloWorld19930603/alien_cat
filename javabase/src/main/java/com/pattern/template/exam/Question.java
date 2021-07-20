package com.pattern.template.exam;

// 试题
public class Question {

  public void que1() {
    System.out.println("第一题：1+1=?");
  }

  public void que2() {
    System.out.println("第二题：1+2=?");
  }

  public void que3() {
    System.out.println("第三题：1+3=?");
  }

  public void doAnswer(String answer) {
    System.out.println("答案：" + answer);
  }
}
