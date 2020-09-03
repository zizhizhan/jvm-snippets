package com.zizhizhan.legacies.pattern.proxy.simple.v0;

class UserProxy implements User {

    private String id;

    public UserProxy(String id) {
        super();
        this.id = id;
    }

    @Override
    public String getId() {
        return id;
    }

    @Override
    public String getName() {
        //select username from usertable where id = {id}
        return "userName";
    }

}
