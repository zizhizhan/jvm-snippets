package com.zizhizhan.legacy.sei;

import javax.xml.ws.WebServiceException;


public abstract class MethodHandler {

    protected final SEIStub owner;

    protected MethodHandler(SEIStub owner) {
        this.owner = owner;
    }

    abstract Object invoke(Object proxy, Object[] args) throws WebServiceException, Throwable;
}

