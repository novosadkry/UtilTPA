package cz.novosadkry.UtilTPA.BungeeCord;

import com.google.common.io.ByteArrayDataInput;
import com.google.common.io.ByteStreams;
import cz.novosadkry.UtilTPA.BungeeCord.Transport.Abstract.Message;
import cz.novosadkry.UtilTPA.BungeeCord.Transport.Abstract.MessageListener;
import org.bukkit.entity.Player;
import org.bukkit.plugin.messaging.PluginMessageListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class BungeeDriver implements PluginMessageListener {
    private static BungeeDriver instance;

    public static BungeeDriver getInstance() {
        if (instance == null)
            instance = new BungeeDriver();

        return instance;
    }

    private final Map<Class<? extends Message>, List<MessageListener>> listeners;

    public BungeeDriver() {
        listeners = new HashMap<>();
    }

    public <T extends MessageListener> void registerListener(Class<? extends Message> type, T listener) {
        listeners.putIfAbsent(type, new ArrayList<>());

        List<MessageListener> list = listeners.get(type);
        list.add(listener);
    }

    public <T extends MessageListener> void unregisterListener(Class<? extends Message> type, T listener) {
        listeners.putIfAbsent(type, new ArrayList<>());

        List<MessageListener> list = listeners.get(type);
        list.remove(listener);
    }

    private void dispatchListeners(Message msg) {
        if (!listeners.containsKey(msg.getClass()))
            return;

        for (MessageListener listener : listeners.get(msg.getClass())) {
            listener.onMessage(msg);
        }
    }

    @Override
    public void onPluginMessageReceived(String channel, Player player, byte[] bytes) {
        if (!channel.equalsIgnoreCase("BungeeCord"))
            return;

        ByteArrayDataInput data = ByteStreams.newDataInput(bytes);
        Message msg = Message.resolve(data);

        assert msg != null;
        dispatchListeners(msg);


//            if (msg instanceof PingMessage) {
//                PingMessage pingMsg = (PingMessage) msg;
//                player.sendMessage(String.format("%s: %s", pingMsg.getFrom(), pingMsg.getMessage()));
//            }
//
//            else if (msg instanceof RequestMessage) {
//                RequestManager requestManager = RequestManager.getInstance();
//
//                RequestMessage requestMsg = (RequestMessage) msg;
//                Request request = new Request(requestMsg.getFrom(), requestMsg.getTo());
//
//                switch (msg.getType()) {
//                    case REQUEST:
//                        requestManager.sendRequest(request);
//                        break;
//
//                    case REQUEST_ACCEPT:
//                        break;
//
//                    case REQUEST_DENY:
//                        break;
//                }
//            }
    }
}
