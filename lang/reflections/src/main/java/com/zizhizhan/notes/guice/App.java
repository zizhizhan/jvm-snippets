package com.zizhizhan.notes.guice;

import com.google.inject.Guice;
import com.google.inject.Injector;

/**
 * Created with IntelliJ IDEA.
 * User: James
 * Date: 13-8-2
 * Time: 3:29 PM
 * To change this template use File | Settings | File Templates.
 */
public class App {

    public static void main(String[] args) {
        Injector injector = Guice.createInjector(new HelloModule());

        HelloService helloService = injector.getInstance(HelloService.class);

        String echo = helloService.say("James");
        System.out.println(echo);
    }

}
