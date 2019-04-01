package me.jameszhan.io.lifecycle;

public class LifecycleSupport {
    private Lifecycle lifecycle;
    private LifecycleListener[] listeners = new LifecycleListener[0];
    private final Object listenersLock = new Object(); // Lock object for changes to listeners

    public LifecycleSupport(Lifecycle lifecycle) {
        this.lifecycle = lifecycle;
    }

    public LifecycleListener[] findLifecycleListeners() {
        return listeners;
    }

    public void fireLifecycleEvent(String type, Object data) {
        LifecycleEvent event = new LifecycleEvent(lifecycle, type, data);
        LifecycleListener[] interested = listeners;
        for (LifecycleListener listener : interested) {
            listener.lifecycle(event);
        }
    }

    public void addLifecycleListener(LifecycleListener listener) {
        synchronized (listenersLock) {
            int length = listeners.length;
            LifecycleListener[] results = new LifecycleListener[length + 1];
            System.arraycopy(listeners, 0, results, 0, length);
            results[length] = listener;
            listeners = results;
        }
    }

    public void removeLifecycleListener(LifecycleListener listener) {
        synchronized (listenersLock) {
            int n = -1, length = listeners.length;
            for (int i = 0; i < length; i++) {
                if (listeners[i] == listener) {
                    n = i;
                    break;
                }
            }

            if (n < 0) {
                return;
            }

            LifecycleListener[] results = new LifecycleListener[listeners.length - 1];
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
