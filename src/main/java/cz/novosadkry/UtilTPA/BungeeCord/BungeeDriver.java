package cz.novosadkry.UtilTPA.BungeeCord;

import com.google.common.io.ByteArrayDataInput;
import com.google.common.io.ByteStreams;
import cz.novosadkry.UtilTPA.BungeeCord.Transport.Abstract.Message;
import cz.novosadkry.UtilTPA.BungeeCord.Transport.Abstract.MessageListener;
import org.bukkit.entity.Player;
import org.bukkit.plugin.messaging.PluginMessageListener;

import java.util.ArrayList;
import java.util.List;

public class BungeeDriver implements PluginMessageListener {
    private static BungeeDriver instance;

    public static BungeeDriver getInstance() {
        if (instance == null)
            instance = new BungeeDriver();

        return instance;
    }

    private final List<MessageListener> listeners;

    public BungeeDriver() {
        listeners = new ArrayList<>();
    }

    public void registerListener(MessageListener listener) {
        listeners.add(listener);
    }

    public void unregisterListener(MessageListener listener) {
        listeners.remove(listener);
    }

    public void unregisterListeners() {
        listeners.clear();
    }

    private void notifyListeners(Message msg) {
        for (MessageListener listener : listeners) {
            listener.onMessage(msg);
        }
    }

    @Override
    public void onPluginMessageReceived(String channel, Player player, byte[] bytes) {
        if (!channel.equalsIgnoreCase("BungeeCord"))
            return;

        ByteArrayDataInput data = ByteStreams.newDataInput(bytes);
        Message msg = Message.resolve(data);

        if (msg == null)
            return;

        notifyListeners(msg);
    }
}
