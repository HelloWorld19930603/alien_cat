package com.pattern.template.exam;

/**
 * 学生对考试题做答类
 */
public class StudentDoAnswer extends Question {

    public void doQue1(String answer){
        que1();
        doAnswer(answer);
    }

    public void doQue2(String answer){
        que2();
        doAnswer(answer);
    }

    public void doQue3(String answer){
        que3();
        doAnswer(answer);
    }

}
