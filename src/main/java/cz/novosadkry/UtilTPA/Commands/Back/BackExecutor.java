package cz.novosadkry.UtilTPA.Commands.Back;

import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class BackExecutor implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (sender instanceof Player) {
            Player player = (Player) sender;

            Location loc = BackPersist.lastLoc.remove(player);

            if (loc != null) {
                player.teleport(loc);
                sender.sendMessage("§aÚspěšně ses vrátil na svoji předešlou pozici");
            }

            else
                sender.sendMessage("§cNení pozice, na kterou by ses mohl vrátit!");
        }

        return true;
    }
}
