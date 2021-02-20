package cz.novosadkry.UtilTPA.Request;

import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.ComponentBuilder;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;

public class RequestManager {
    private static RequestManager manager;
    private HashMap<Player, LinkedList<Request>> requests = new HashMap<>();

    public HashMap<Player, LinkedList<Request>> getAll() {
        return requests;
    }

    public LinkedList<Request> get(Player player) {
        return requests.get(player);
    }

    // Get: O(1), Remove: O(1)
    public Request pop(Player to) {
        return requests.get(to).pop();
    }

    // Get: O(n), Remove: O(1)
    public Request popFrom(Player to, Player from) {
        Request request = null;

        Iterator<Request> it = requests.get(to).iterator();
        while (it.hasNext()) {
            Request r = it.next();

            if (r.getFrom() == from) {
                request = r;
                it.remove();
                break;
            }
        }

        return request;
    }

    public boolean hasRequests(Player to) {
        return requests.containsKey(to) && !requests.get(to).isEmpty();
    }

    public boolean hasRequest(Player to, Request request) {
        return requests.containsKey(to) && requests.get(to).contains(request);
    }

    public boolean sendRequest(Request request) {
        if (hasRequest(request.to, request))
            return false;

        requests.get(request.to).add(request);
        request.startCountdown();

        request.from.sendMessage("§bPoslal si teleport request hráčovi §e" + request.to.getName());

        TextComponent tpaccept = new TextComponent( "§a/tpaccept" );
        TextComponent tpdeny = new TextComponent( "§4/tpdeny" );

        tpaccept.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/tpaccept " + request.from.getName()));
        tpaccept.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new ComponentBuilder("Kliknutím příjmeš request").create()));
        tpdeny.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/tpdeny  " + request.from.getName()));
        tpdeny.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new ComponentBuilder("Kliknutím odmítneš request").create()));

        ComponentBuilder target_msg = new ComponentBuilder("§bByl ti poslán teleport request od hráče ")
                .append("§e" + request.from.getName())
                .append("\n§bNa odpověd' máš §e20 §bsekund")
                .append("\n\n§bPro příjmutí napiš ")
                .append(tpaccept)
                .append("\n§bPro odmítnutí napiš ")
                .append(tpdeny);

        request.to.spigot().sendMessage(target_msg.create());
        return true;
    }

    public static RequestManager getInstance() {
        if (manager == null)
            manager = new RequestManager();

        return manager;
    }
}
