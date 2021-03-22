package cz.novosadkry.UtilTPA.BungeeCord.Drivers;

import cz.novosadkry.UtilTPA.BungeeCord.Transport.Messages.Message;
import cz.novosadkry.UtilTPA.BungeeCord.Transport.Messages.MessageEventHandler;
import cz.novosadkry.UtilTPA.Main;
import org.bukkit.Bukkit;

public abstract class BungeeDriver extends MessageEventHandler implements IBungeeDriver {

    public void initialize() {
        Bukkit.getMessenger().registerIncomingPluginChannel(Main.getInstance(), "BungeeCord", this);
        Bukkit.getMessenger().registerOutgoingPluginChannel(Main.getInstance(), "BungeeCord");
    }

    @Override
    public void sendMessage(Message msg) {
        Bukkit.getServer().sendPluginMessage(Main.getInstance(), "BungeeCord", msg.toBytes());
    }

    public void terminate() {
        unregisterListeners();
    }
}
