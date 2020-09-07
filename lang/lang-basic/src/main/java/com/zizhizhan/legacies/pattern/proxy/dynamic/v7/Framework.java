package com.zizhizhan.legacies.pattern.proxy.dynamic.v7;

class Framework {

    Request req;

    public void init(Context context) {
        req = context.getRequest();
    }

    public void service() {
        System.out.println(req.getId());
    }

    public void destroy() {
        req = null;
    }
}

