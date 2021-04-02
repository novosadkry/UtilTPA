package cz.novosadkry.UtilTPA.Commands.TPA.TabCompleters;

import cz.novosadkry.UtilTPA.Requests.Managers.IRequestManager;
import cz.novosadkry.UtilTPA.Requests.Request;
import cz.novosadkry.UtilTPA.Requests.Managers.RequestManager;
import cz.novosadkry.UtilTPA.Requests.RequestPlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;

import java.util.List;
import java.util.stream.Collectors;

public class TpAcceptTabCompleter implements TabCompleter {
    @Override
    public List<String> onTabComplete(CommandSender sender, Command cmd, String label, String[] args) {
        IRequestManager requestManager = RequestManager.getInstance();
        List<Request> requests = requestManager.getAllPlayer(new RequestPlayer(sender.getName()));

        return requests.stream()
                .map(r -> r.getFrom().getName())
                .collect(Collectors.toList());
    }
}
