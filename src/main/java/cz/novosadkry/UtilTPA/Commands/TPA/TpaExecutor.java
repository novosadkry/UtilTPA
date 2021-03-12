package cz.novosadkry.UtilTPA.Commands.TPA;

import cz.novosadkry.UtilTPA.Config;
import cz.novosadkry.UtilTPA.Request.Request;
import cz.novosadkry.UtilTPA.Request.RequestManager;
import cz.novosadkry.UtilTPA.UI.RequestInventory;
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
                Request request = new Request(player.getName(), args[0]);
                requestManager.sendRequest(request);
            }

            else if (args.length == 0 && Config.getBoolean("head-inventory.enabled"))
                new RequestInventory(player).openInventory();

            else
                player.sendMessage("§cZadal si nesprávný počet argumentů!");
        }

        else
            sender.sendMessage("Tento prikaz nelze vyvolat z konzole");

        return true;
    }

}
