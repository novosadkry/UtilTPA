package cz.novosadkry.UtilTPA.BungeeCord.Drivers;

import cz.novosadkry.UtilBungee.Transport.Messages.IMessage;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.List;
import java.util.stream.Collectors;

public class BungeeDriverOffline extends BungeeDriver {
    public BungeeDriverOffline() {
        super(null);
    }

    public String getServerName() {
        return null;
    }

    public List<String> getPlayerList() {
        return Bukkit.getOnlinePlayers().stream()
                .map(Player::getName)
                .collect(Collectors.toList());
    }

    @Override
    public void sendMessage(IMessage msg) { }

    @Override
    public void initialize() { }

    public void terminate() {
        unregisterListeners();
    }

    @Override
    public void onPluginMessageReceived(String s, Player player, byte[] bytes) { }
}
