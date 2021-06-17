package com.aliencat.javabase.spider;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;

public class SeleniumDemo {
    public static void main(String[] args) {
        // 第一步： 设置chromedriver地址。没有的请去chrome官网下
        // 下载地址：http://chromedriver.storage.googleapis.com/index.html。
        System.setProperty("webdriver.chrome.driver",
                "C:\\Program Files\\Google\\Chrome\\Application\\chromedriver.exe");
        // 第二步：初始化驱动
        WebDriver driver = new ChromeDriver();
        // 第三步：获取目标网页
        driver.get("http://blog.csdn.net/");
        // 第四步：解析。以下就可以进行解了。使用webMagic、jsoup等进行必要的解析。
        System.out.println("网页标题: " + driver.getTitle());
        System.out.println("网页当前地址: " + driver.getCurrentUrl());
        System.out.println("源码: \n" + driver.getPageSource());

    }
}

