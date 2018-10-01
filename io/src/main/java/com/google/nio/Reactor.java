package com.google.nio;

import java.nio.channels.SelectionKey;


public interface Reactor {
	
	void register(ReactorHandler handler);

    void deregister(ReactorHandler handler);

    void interest(ReactorHandler handler, int ops);

    int OP_READ = SelectionKey.OP_READ;
    int OP_NON_WRITE = 1 << 1;
    int OP_WRITE = SelectionKey.OP_WRITE;
    int OP_CONNECT = SelectionKey.OP_CONNECT;
    int OP_ACCEPT = SelectionKey.OP_ACCEPT;

}
