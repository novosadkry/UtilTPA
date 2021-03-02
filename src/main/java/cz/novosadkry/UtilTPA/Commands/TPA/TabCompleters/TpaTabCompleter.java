package cz.novosadkry.UtilTPA.Commands.TPA.TabCompleters;

import cz.novosadkry.UtilTPA.BungeeCord.Drivers.BungeeDriver;
import cz.novosadkry.UtilTPA.Main;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;

import java.util.List;
import java.util.stream.Collectors;

public class TpaTabCompleter implements TabCompleter {
    @Override
    public List<String> onTabComplete(CommandSender sender, Command cmd, String label, String[] args) {
        BungeeDriver bungeeDriver = Main.getInstance().getBungeeDriver();
        List<String> playerList = bungeeDriver.getPlayerList();

        return playerList.stream()
                .filter(p -> !p.equals(sender.getName()))
                .collect(Collectors.toList());
    }
}
