package cz.novosadkry.UtilTPA.BungeeCord.Drivers.Concrete;

import cz.novosadkry.UtilTPA.BungeeCord.Drivers.BungeeDriver;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.List;
import java.util.stream.Collectors;

public class BungeeDriverOffline extends BungeeDriver {
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
    public void initialize() { }

    @Override
    public void onPluginMessageReceived(String channel, Player player, byte[] bytes) { }
}
