package cz.novosadkry.UtilTPA.Commands.TPA;

import cz.novosadkry.UtilTPA.Request.Request;
import cz.novosadkry.UtilTPA.Request.RequestManager;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class TpDenyExecutor implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (sender instanceof Player) {
            Player player = (Player)sender;
            RequestManager requestManager = RequestManager.getInstance();

            if (requestManager.hasRequests(player)) {
                Request request;

                if (args.length == 1) {
                    request = requestManager.popFrom(player, org.bukkit.Bukkit.getPlayer(args[0]));

                    if (request == null) {
                        sender.sendMessage("§cNemáš žádné příchozí requesty od hráče §e" + args[0]);
                        return true;
                    }
                }

                else {
                    request = requestManager.pop(player);
                }

                player.sendMessage("§cOdmítl si request hráče §e" + request.getFrom().getName());
                request.getFrom().sendMessage("§cHráč §e" + request.getTo().getName() + " §codmítl tvůj request.");
                request.cancelCountdown();

                return true;
            }

            sender.sendMessage("§cNemáš žádné příchozí requesty!");
        }

        return true;
    }

}