package com.zizhizhan.legacies.pattern.proxy.dynamic.v7;

class RequestImp implements Request {

    private String id;

    public RequestImp(String id) {
        super();
        this.id = id;
    }

    @Override
    public String getId() {
        return id;
    }

}
