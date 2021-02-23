package cz.novosadkry.UtilTPA.Request;

import cz.novosadkry.UtilTPA.Commands.Back.BackPersist;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.ComponentBuilder;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.LinkedList;

public class RequestManager {
    private static RequestManager manager;
    private final HashMap<Player, LinkedList<Request>> requests = new HashMap<>();

    public HashMap<Player, LinkedList<Request>> getAll() {
        return requests;
    }

    public LinkedList<Request> getAllPlayer(Player player) {
        return requests.get(player);
    }

    public Request get(Player to) {
        return requests.get(to).peek();
    }

    public Request getFrom(Player to, Player from) {
        return requests.get(to).stream()
                .filter(r -> r.from == from)
                .findFirst().orElse(null);
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

        getAllPlayer(request.to).add(request);
        request.startCountdown();

        request.from.sendMessage("§bPoslal si teleport request hráčovi §e" + request.to.getName());

        TextComponent tpAccept = new TextComponent( "§a/tpaccept" );
        TextComponent tpDeny = new TextComponent( "§4/tpdeny" );

        tpAccept.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/tpaccept " + request.from.getName()));
        tpAccept.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new ComponentBuilder("Kliknutím příjmeš request").create()));
        tpDeny.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/tpdeny  " + request.from.getName()));
        tpDeny.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new ComponentBuilder("Kliknutím odmítneš request").create()));

        ComponentBuilder targetMsg = new ComponentBuilder("§bByl ti poslán teleport request od hráče ")
                .append("§e" + request.from.getName())
                .append("\n§bNa odpověd' máš §e20 §bsekund")
                .append("\n\n§bPro příjmutí napiš ")
                .append(tpAccept)
                .append("\n§bPro odmítnutí napiš ")
                .append(tpDeny);

        request.to.spigot().sendMessage(targetMsg.create());
        return true;
    }

    public void timeoutRequest(Request request) {
        getAllPlayer(request.to).remove(request);
        request.from.sendMessage("§cHráč §e"+ request.to.getName() +"§c neodpověděl na tvůj request.");
        request.to.sendMessage("§cNeodpověděl si na request hráče §e"+ request.from.getName());
    }

    public void acceptRequest(Request request) {
        getAllPlayer(request.to).remove(request);
        BackPersist.lastLoc.put(request.from, request.from.getLocation());
        request.from.teleport(request.getTo().getLocation());
        request.from.sendMessage("§aHráč §e" + request.to.getName() + " §apřijal tvůj request.");
        request.to.sendMessage("§aPřijal si request hráče §e" + request.from.getName());
        request.cancelCountdown();
    }

    public void denyRequest(Request request) {
        getAllPlayer(request.to).remove(request);
        request.from.sendMessage("§cHráč §e" + request.to.getName() + " §codmítl tvůj request.");
        request.to.sendMessage("§cOdmítl si request hráče §e" + request.from.getName());
        request.cancelCountdown();
    }

    public static RequestManager getInstance() {
        if (manager == null)
            manager = new RequestManager();

        return manager;
    }
}
