package com.zizhizhan.notes.guice;

import com.google.inject.Inject;

public class HelloServiceImp implements HelloService {

    @Inject
    public HelloServiceImp(@Hello Prompt prompt){
        System.out.println("prompt => " + prompt);
    }

    public String say(String msg) {
        return "Hello " + msg;
    }



}


