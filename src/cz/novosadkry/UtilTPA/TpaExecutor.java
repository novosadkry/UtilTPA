package cz.novosadkry.UtilTPA;

import cz.novosadkry.UtilTPA.Request.Request;
import cz.novosadkry.UtilTPA.Request.RequestManager;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.ComponentBuilder;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.LinkedList;

public class TpaExecutor implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (sender instanceof Player) {
            if (args.length == 1) {
                Player target = org.bukkit.Bukkit.getPlayer(args[0]);
                RequestManager requestManager = RequestManager.getInstance();

                if (target != null && target.isOnline() && target.isValid() && target != (Player)sender) {
                    requestManager.getAll().computeIfAbsent(target, k -> new LinkedList<>());

                    if (requestManager.getAll().containsKey(target)) {
                        Request request = new Request((Player)sender, target);

                        if (!requestManager.get(target).contains(request)) {
                            requestManager.get(target).add(request);
                            request.startCountdown();

                            sender.sendMessage("§bPoslal si teleport request hráčovi §e" + target.getName());

                            TextComponent tpaccept = new TextComponent( "§a/tpaccept" );
                            TextComponent tpdeny = new TextComponent( "§4/tpdeny" );

                            tpaccept.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/tpaccept " + sender.getName()));
                            tpaccept.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new ComponentBuilder("Kliknutím příjmeš request").create()));
                            tpdeny.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/tpdeny  " + sender.getName()));
                            tpdeny.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new ComponentBuilder("Kliknutím odmítneš request").create()));

                            ComponentBuilder target_msg = new ComponentBuilder("§bByl ti poslán teleport request od hráče ")
                                    .append("§e" + sender.getName())
                                    .append("\n§bNa odpověd' máš §e20 §bsekund")
                                    .append("\n\n§bPro příjmutí napiš ")
                                    .append(tpaccept)
                                    .append("\n§bPro odmítnutí napiš ")
                                    .append(tpdeny);

                            target.spigot().sendMessage(target_msg.create());
                        }

                        else {
                            sender.sendMessage("§cTomuhle hráčovi už si request poslal!");
                        }
                    }
                }

                else {
                    sender.sendMessage("§cHráč je offline, mrtev, nebo neexistuje!");
                }
            }

            else {
                sender.sendMessage("§cZadal si nesprávný počet argumentů!");
            }
        }

        else {
            sender.sendMessage("Tento prikaz nelze vyvolat z konzole");
        }

        return true;
    }

}
