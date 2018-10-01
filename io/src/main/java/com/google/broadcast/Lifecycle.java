package com.google.broadcast;



public interface Lifecycle {	

    public static final String INIT_EVENT = "init";

    public static final String START_EVENT = "start";

    public static final String BEFORE_START_EVENT = "before_start";

    public static final String AFTER_START_EVENT = "after_start";

    public static final String STOP_EVENT = "stop";

    public static final String BEFORE_STOP_EVENT = "before_stop";

    public static final String AFTER_STOP_EVENT = "after_stop";

    public static final String DESTROY_EVENT = "destroy";

    public static final String PERIODIC_EVENT = "periodic";

    public void addLifecycleListener(LifecycleListener listener);

    public LifecycleListener[] findLifecycleListeners();

    public void removeLifecycleListener(LifecycleListener listener);

    public void start() throws LifecycleException;

    public void stop() throws LifecycleException;


}
