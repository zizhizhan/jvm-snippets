package com.zizhizhan.legacies.pattern.proxy.dynamic.v7;

public class Main {

    public static void main(String[] args) {
        Context context = new Context();

        Framework framework = new Framework();
        framework.init(context);

        context.setRequest(new RequestImp("peter"));
        framework.service();

        context.setRequest(new RequestImp("james"));
        framework.service();

        context.setRequest(new RequestImp("thomas"));
        framework.service();
    }

}