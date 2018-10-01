package me.jameszhan.io.framework.broadcast;

public class LifecycleSupport {

    private Lifecycle lifecycle;
    private LifecycleListener listeners[] = new LifecycleListener[0];
    private final Object listenersLock = new Object(); // Lock object for changes to listeners

    public LifecycleSupport(Lifecycle lifecycle) {
        this.lifecycle = lifecycle;
    }

    public void addLifecycleListener(LifecycleListener listener) {
        synchronized (listenersLock) {
            LifecycleListener results[] = new LifecycleListener[listeners.length + 1];
            for (int i = 0; i < listeners.length; i++) {
                results[i] = listeners[i];
            }
            results[listeners.length] = listener;
            listeners = results;
        }
    }

    public LifecycleListener[] findLifecycleListeners() {
        return listeners;
    }

    public void fireLifecycleEvent(String type, Object data) {
        LifecycleEvent event = new LifecycleEvent(lifecycle, type, data);
        LifecycleListener interested[] = listeners;
        for (int i = 0; i < interested.length; i++) {
            interested[i].lifecycle(event);
        }
    }


    public void removeLifecycleListener(LifecycleListener listener) {
        synchronized (listenersLock) {
            int n = -1;
            for (int i = 0; i < listeners.length; i++) {
                if (listeners[i] == listener) {
                    n = i;
                    break;
                }
            }
            if (n < 0)
                return;
            LifecycleListener results[] = new LifecycleListener[listeners.length - 1];
            int j = 0;
            for (int i = 0; i < listeners.length; i++) {
                if (i != n) {
                    results[j++] = listeners[i];
                }
            }
            listeners = results;
        }
    }


}
