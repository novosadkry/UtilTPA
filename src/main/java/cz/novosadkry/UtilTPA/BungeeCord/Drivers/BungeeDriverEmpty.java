package cz.novosadkry.UtilTPA.BungeeCord.Drivers;

import cz.novosadkry.UtilTPA.BungeeCord.Transport.Abstract.MessageListener;
import org.bukkit.entity.Player;

public class BungeeDriverEmpty implements BungeeDriver {
    @Override
    public String getServerName() {
        return null;
    }

    @Override
    public void askForServerName() {

    }

    @Override
    public void registerListener(MessageListener listener) {

    }

    @Override
    public void unregisterListener(MessageListener listener) {

    }

    @Override
    public void unregisterListeners() {

    }

    @Override
    public void onPluginMessageReceived(String channel, Player player, byte[] bytes) {

    }
}
