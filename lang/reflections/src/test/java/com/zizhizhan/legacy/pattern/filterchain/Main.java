package com.zizhizhan.legacy.pattern.filterchain;

import org.springframework.context.support.ClassPathXmlApplicationContext;

public class Main {

    public static void main(String[] args) throws InterruptedException {
        ClassPathXmlApplicationContext ac = new ClassPathXmlApplicationContext(
        		"/spring-task.xml", Main.class);

        Thread.sleep(600000);

        ac.close();
    }

}
