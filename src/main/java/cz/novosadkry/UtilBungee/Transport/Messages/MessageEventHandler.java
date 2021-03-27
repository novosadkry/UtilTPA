package cz.novosadkry.UtilBungee.Transport.Messages;

import java.util.Iterator;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class MessageEventHandler implements IMessageEventHandler {
    private final Map<Class<?>, IMessageListener> listeners;

    public MessageEventHandler() {
        listeners = new ConcurrentHashMap<>();
    }

    @Override
    public MessageEventHandler registerListener(IMessageListener listener) {
        listeners.put(listener.getClass(), listener);
        return this;
    }

    @Override
    public MessageEventHandler unregisterListener(IMessageListener listener) {
        listeners.remove(listener.getClass());
        return this;
    }

    @Override
    public MessageEventHandler unregisterListeners() {
        listeners.clear();
        return this;
    }

    protected void notifyListeners(IMessage msg) {
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
