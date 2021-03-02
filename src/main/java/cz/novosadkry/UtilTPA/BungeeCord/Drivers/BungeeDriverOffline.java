package cz.novosadkry.UtilTPA.BungeeCord.Drivers;

import cz.novosadkry.UtilTPA.BungeeCord.Transport.Abstract.Message;
import cz.novosadkry.UtilTPA.BungeeCord.Transport.Abstract.MessageListener;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.List;
import java.util.stream.Collectors;

public class BungeeDriverOffline implements BungeeDriver {
    @Override
    public String getServerName() {
        return null;
    }

    @Override
    public List<String> getPlayerList() {
        return Bukkit.getOnlinePlayers().stream()
                .map(Player::getName)
                .collect(Collectors.toList());
    }

    @Override
    public void initialize() {

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
    public void sendMessage(Message msg) {

    }

    @Override
    public void onPluginMessageReceived(String channel, Player player, byte[] bytes) {

    }

    @Override
    public void terminate() {}
}
