package com.zizhizhan.legacies.pattern.observer.v3;

class SomeListener implements FooListener {

    private String msg;

    public SomeListener(String msg) {
        super();
        this.msg = msg;
    }

    @Override
    public void foo() {
        System.out.println(msg);
    }

}
