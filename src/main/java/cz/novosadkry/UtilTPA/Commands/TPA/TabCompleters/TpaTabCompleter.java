package cz.novosadkry.UtilTPA.Commands.TPA.TabCompleters;

import cz.novosadkry.UtilTPA.Main;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;

import java.util.List;
import java.util.stream.Collectors;

public class TpaTabCompleter implements TabCompleter {
    @Override
    public List<String> onTabComplete(CommandSender sender, Command cmd, String label, String[] args) {
        List<String> playerList = Main.getInstance().getBungeeDriver().getPlayerList();

        return (playerList != null)
                ? playerList
                : Bukkit.getOnlinePlayers().stream()
                        .map(Player::getName)
                        .collect(Collectors.toList());
    }
}
