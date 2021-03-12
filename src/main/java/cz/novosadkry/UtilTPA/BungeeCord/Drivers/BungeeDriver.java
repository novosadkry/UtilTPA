package cz.novosadkry.UtilTPA.BungeeCord.Drivers;

import cz.novosadkry.UtilTPA.BungeeCord.Transport.Messages.Message;
import cz.novosadkry.UtilTPA.BungeeCord.Transport.Messages.MessageEventHandler;
import cz.novosadkry.UtilTPA.Main;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.messaging.PluginMessageListener;

import java.util.List;

public abstract class BungeeDriver extends MessageEventHandler implements PluginMessageListener {
    abstract public String getServerName();

    abstract public List<String> getPlayerList();

    public void initialize() {
        Bukkit.getMessenger().registerIncomingPluginChannel(Main.getInstance(), "BungeeCord", this);
        Bukkit.getMessenger().registerOutgoingPluginChannel(Main.getInstance(), "BungeeCord");
    }

    public void sendMessage(Message msg) {
        Bukkit.getServer().sendPluginMessage(Main.getInstance(), "BungeeCord", msg.toBytes());
    }

    @Override
    abstract public void onPluginMessageReceived(String channel, Player player, byte[] bytes);

    public void terminate() {
        unregisterListeners();
    }
}
