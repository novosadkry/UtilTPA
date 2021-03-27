package cz.novosadkry.UtilTPA.Commands.Back;

import cz.novosadkry.UtilTPA.BungeeCord.Drivers.BungeeDriver;
import cz.novosadkry.UtilTPA.BungeeCord.Transport.Messages.ConnectMessage;
import cz.novosadkry.UtilTPA.Main;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import static cz.novosadkry.UtilTPA.Localization.Locale.*;

public class BackExecutor implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (sender instanceof Player) {
            Player player = (Player) sender;

            BackInfo backInfo = BackPersist.getLastLoc().remove(player);

            if (backInfo == null)
                sender.sendMessage(tl("back.failure"));

            else if (backInfo.getLoc() != null) {
                player.teleport(backInfo.getLoc());
                sender.sendMessage(tl("back.success"));
            }

            else if (backInfo.getServer() != null) {
                new ConnectMessage(player, backInfo.getServer())
                        .send(Main.getService(BungeeDriver.class));
            }

            else
                sender.sendMessage(tl("back.failure"));
        }

        return true;
    }
}
