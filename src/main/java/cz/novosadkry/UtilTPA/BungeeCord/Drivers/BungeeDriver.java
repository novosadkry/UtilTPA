package cz.novosadkry.UtilTPA.BungeeCord.Drivers;

import cz.novosadkry.UtilTPA.BungeeCord.Transport.Abstract.Message;
import cz.novosadkry.UtilTPA.BungeeCord.Transport.Abstract.MessageListener;
import org.bukkit.entity.Player;
import org.bukkit.plugin.messaging.PluginMessageListener;

public interface BungeeDriver extends PluginMessageListener {
    String getServerName();

    void initialize();

    void askForServerName();

    void registerListener(MessageListener listener);

    void unregisterListener(MessageListener listener);

    void unregisterListeners();

    void sendMessage(Message msg);

    @Override
    void onPluginMessageReceived(String channel, Player player, byte[] bytes);
}
