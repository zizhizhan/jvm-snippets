package com.zizhizhan.legacies.thirdparty.guice;

import com.google.inject.AbstractModule;

/**
 * Created with IntelliJ IDEA.
 * User: James
 * Date: 13-8-2
 * Time: 3:38 PM
 * To change this template use File | Settings | File Templates.
 */
public class HelloModule extends AbstractModule {

    @Override
    protected void configure() {
        bind(Prompt.class).annotatedWith(Hello.class).to(HelloPrompt.class);
        bind(HelloService.class).to(HelloServiceImp.class);
    }

}
