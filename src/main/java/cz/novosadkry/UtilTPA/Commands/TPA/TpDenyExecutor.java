package cz.novosadkry.UtilTPA.Commands.TPA;

import cz.novosadkry.UtilTPA.Localization.PlaceHolder;
import cz.novosadkry.UtilTPA.Request.Request;
import cz.novosadkry.UtilTPA.Request.RequestManager;
import cz.novosadkry.UtilTPA.Request.RequestPlayer;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import static cz.novosadkry.UtilTPA.Localization.Locale.*;

public class TpDenyExecutor implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (sender instanceof Player) {
            RequestPlayer player = new RequestPlayer((Player) sender);
            RequestManager requestManager = RequestManager.getInstance();

            if (requestManager.hasRequests(player)) {
                Request request;

                if (args.length == 1) {
                    Player target = Bukkit.getPlayerExact(args[0]);
                    request = requestManager.getFrom(player, new RequestPlayer(args[0]));

                    if (request == null) {
                        player.getPlayer().sendMessage(tl("requests.error.hasNoneFrom", new PlaceHolder("player", args[0])));
                        return true;
                    }

                    if (target == null) {
                        requestManager.denyRequest(request);
                        return true;
                    }
                }

                else {
                    request = requestManager.get(player);
                }

                requestManager.denyRequest(request);
                return true;
            }

            player.getPlayer().sendMessage(tl("requests.error.hasNone"));
        }

        return true;
    }

}
