package cz.novosadkry.UtilTPA.Commands.TPA;

import cz.novosadkry.UtilTPA.Request.Request;
import cz.novosadkry.UtilTPA.Request.RequestManager;
import cz.novosadkry.UtilTPA.UI.RequestInventory;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class TpaExecutor implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (sender instanceof Player) {
            Player player = (Player)sender;
            RequestManager requestManager = RequestManager.getInstance();

            if (args.length == 1) {
                Player target = Bukkit.getPlayer(args[0]);

                if (target != null && target.isOnline() && target.isValid() && target != player) {
                    Request request = new Request(player, target);

                    if (!requestManager.sendRequest(request))
                        player.sendMessage("§cTomuhle hráči už si request poslal!");
                }

                else
                    player.sendMessage("§cHráč je offline, mrtev, nebo neexistuje!");
            }

            else if (args.length == 0)
                new RequestInventory(player).openInventory();

            else
                player.sendMessage("§cZadal si nesprávný počet argumentů!");
        }

        else
            sender.sendMessage("Tento prikaz nelze vyvolat z konzole");

        return true;
    }

}
