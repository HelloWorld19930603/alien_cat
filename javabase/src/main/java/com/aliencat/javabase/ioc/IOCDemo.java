package com.aliencat.javabase.ioc;

public class IOCDemo {

    public static void main(String[] args) throws Exception {
        //读取User的XML配置文件
        ClassPathXmlApplicationContext appLication = new ClassPathXmlApplicationContext("user.xml");
        //获取User的Bean对象
        Object bean = appLication.getBean("user1");
        User user = (User) bean;
        System.out.println(user);

        //获取User的Bean对象
        bean = appLication.getBean("user2");
        user = (User) bean;
        System.out.println(user);
    }
}
