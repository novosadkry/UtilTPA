package cz.novosadkry.UtilTPA.BungeeCord.Drivers;

import cz.novosadkry.UtilTPA.BungeeCord.Transport.Abstract.MessageListener;
import org.bukkit.entity.Player;
import org.bukkit.plugin.messaging.PluginMessageListener;

public interface BungeeDriver extends PluginMessageListener {
    String getServerName();

    void askForServerName();

    void registerListener(MessageListener listener);

    void unregisterListener(MessageListener listener);

    void unregisterListeners();

    @Override
    void onPluginMessageReceived(String channel, Player player, byte[] bytes);
}
