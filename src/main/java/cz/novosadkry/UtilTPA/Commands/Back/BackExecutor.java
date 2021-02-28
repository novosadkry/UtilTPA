package cz.novosadkry.UtilTPA.Commands.Back;

import cz.novosadkry.UtilTPA.BungeeCord.Transport.Messages.ConnectMessage;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class BackExecutor implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (sender instanceof Player) {
            Player player = (Player) sender;

            BackInfo backInfo = BackPersist.getLastLoc().remove(player);

            if (backInfo.getLoc() != null) {
                player.teleport(backInfo.getLoc());
                sender.sendMessage("§aÚspěšně ses vrátil na svoji předešlou pozici");
            }

            else if (backInfo.getServer() != null) {
                new ConnectMessage(player, backInfo.getServer()).send();
            }

            else
                sender.sendMessage("§cNení pozice, na kterou by ses mohl vrátit!");
        }

        return true;
    }
}
