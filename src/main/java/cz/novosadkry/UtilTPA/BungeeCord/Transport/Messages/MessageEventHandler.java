package cz.novosadkry.UtilTPA.BungeeCord.Transport.Messages;

import cz.novosadkry.UtilTPA.Log;

import java.util.Iterator;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class MessageEventHandler {
    private final Map<Class<?>, IMessageListener> listeners;

    public MessageEventHandler() {
        listeners = new ConcurrentHashMap<>();
    }

    public MessageEventHandler registerListener(IMessageListener listener) {
        if (listeners.put(listener.getClass(), listener) == null)
            Log.fine(getClass().getSimpleName() + " : Register listener of type " + listener.getClass().getName());

        return this;
    }

    public MessageEventHandler unregisterListener(IMessageListener listener) {
        listeners.remove(listener.getClass());
        return this;
    }

    public MessageEventHandler unregisterListeners() {
        listeners.clear();
        return this;
    }

    protected void notifyListeners(Message msg) {
        synchronized (listeners) {
            Iterator<Map.Entry<Class<?>, IMessageListener>> it = listeners.entrySet().iterator();

            while (it.hasNext()) {
                Map.Entry<Class<?>, IMessageListener> entry = it.next();
                entry.getValue().onMessage(msg);

                if (entry.getKey().isAnonymousClass())
                    it.remove();
            }
        }
    }
}
