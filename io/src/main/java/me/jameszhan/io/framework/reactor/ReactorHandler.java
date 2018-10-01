package me.jameszhan.io.framework.reactor;

import java.nio.channels.SelectableChannel;



public interface ReactorHandler {
	
	Session getSession();

    SelectableChannel[] getChannels();

    void onRegistered();

    void onDeregistered();

    void onReadable();

    void onWritable();

    void onAcceptable();

    void onConnectable();

    void onTimeout();

}
