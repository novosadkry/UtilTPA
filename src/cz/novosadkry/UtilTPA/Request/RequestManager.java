package cz.novosadkry.UtilTPA.Request;

import cz.novosadkry.UtilTPA.BungeeCord.Transport.RequestDenyMessage;
import cz.novosadkry.UtilTPA.Commands.Back.BackPersist;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.ComponentBuilder;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.entity.Player;

import java.util.Map;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Queue;

public class RequestManager {
    private static RequestManager manager;
    private final Map<String, LinkedList<Request>> requests = new HashMap<>();
    private final Queue<Request> remoteRequests = new LinkedList<Request>();

    public Map<String, LinkedList<Request>> getAll() {
        return requests;
    }

    public Queue<Request> getRemote() {
        return remoteRequests;
    }

    public LinkedList<Request> getAllPlayer(String player) {
        return requests.get(player);
    }

    public LinkedList<Request> getAllPlayer(Player player) {
        return getAllPlayer(player.getName());
    }

    public Request get(String to) {
        return requests.get(to).peek();
    }

    public Request get(Player to) {
        return get(to.getName());
    }

    public Request getFrom(String to, String from) {
        return requests.get(to).stream()
                .filter(r -> r.getFrom().equals(from))
                .findFirst().orElse(null);
    }

    public Request getFrom(Player to, Player from) {
        return getFrom(to.getName(), from.getName());
    }

    public boolean hasRequests(String to) {
        return requests.containsKey(to) && !requests.get(to).isEmpty();
    }

    public boolean hasRequests(Player to) {
        return hasRequests(to.getName());
    }

    public boolean hasRequest(String to, Request request) {
        return requests.containsKey(to) && requests.get(to).contains(request);
    }

    public boolean hasRequest(Player to, Request request) {
        return hasRequest(to.getName(), request);
    }

    public void sendRequest(Request request) {
        getAll().computeIfAbsent(request.getTo(), k -> new LinkedList<>());

        final Player fromPlayer = request.getFromPlayer();
        final Player toPlayer = request.getToPlayer();

        if (hasRequest(request.getTo(), request))
        {
            if (fromPlayer == null)
            {
                new RequestDenyMessage(request)
                        .setReason("§cTomuhle hráči už jsi request poslal!")
                        .send();
            }

            else
                fromPlayer.sendMessage("§cTomuhle hráči už jsi request poslal!");

            return;
        }

        // Player is probably on another server
        if (toPlayer == null) {
            remoteRequests.add(request);
        }

        else {
            getAllPlayer(request.getTo()).add(request);
            request.startCountdown();

            TextComponent tpAccept = new TextComponent( "§a/tpaccept" );
            TextComponent tpDeny = new TextComponent( "§4/tpdeny" );

            tpAccept.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/tpaccept " + request.getFrom()));
            tpAccept.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new ComponentBuilder("Kliknutím příjmeš request").create()));
            tpDeny.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/tpdeny  " + request.getFrom()));
            tpDeny.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new ComponentBuilder("Kliknutím odmítneš request").create()));

            ComponentBuilder targetMsg = new ComponentBuilder("\n§bByl ti poslán teleport request od hráče ")
                    .append("§e" + request.getFrom())
                    .append("\n§bNa odpověd' máš §e20 §bsekund")
                    .append("\n\n§bPro příjmutí napiš ")
                    .append(tpAccept)
                    .append("\n§bPro odmítnutí napiš ")
                    .append(tpDeny);

            toPlayer.spigot().sendMessage(targetMsg.create());

            if (fromPlayer != null)
                fromPlayer.sendMessage("§bPoslal si teleport request hráčovi §e" + request.getTo());
        }
    }

    public void timeoutRequest(Request request) {
        getAllPlayer(request.getTo()).remove(request);

        final Player fromPlayer = request.getFromPlayer();
        final Player toPlayer = request.getToPlayer();

        if (fromPlayer != null)
            fromPlayer.sendMessage("§cHráč §e"+ request.getTo() +"§c neodpověděl na tvůj request.");

        if (toPlayer != null)
            toPlayer.sendMessage("§cNeodpověděl si na request hráče §e"+ request.getFrom());
    }

    public void acceptRequest(Request request) {
        getAllPlayer(request.getTo()).remove(request);

        final Player fromPlayer = request.getFromPlayer();
        final Player toPlayer = request.getToPlayer();

        if (fromPlayer != null) {
            BackPersist.lastLoc.put(fromPlayer, fromPlayer.getLocation());

            fromPlayer.teleport(toPlayer.getLocation());
            fromPlayer.sendMessage("§aHráč §e" + request.getTo() + " §apřijal tvůj request.");
        }

        if (toPlayer != null)
            toPlayer.sendMessage("§aPřijal jsi request hráče §e" + request.getFrom());

        request.cancelCountdown();
    }

    public void denyRequest(Request request) {
        getAllPlayer(request.getTo()).remove(request);

        final Player fromPlayer = request.getFromPlayer();
        final Player toPlayer = request.getToPlayer();

        if (fromPlayer != null)
            fromPlayer.sendMessage("§cHráč §e" + request.getTo() + " §codmítnul tvůj request.");

        if (toPlayer != null)
            toPlayer.sendMessage("§cOdmítnul jsi request hráče §e" + request.getFrom());

        request.cancelCountdown();
    }

    public static RequestManager getInstance() {
        if (manager == null)
            manager = new RequestManager();

        return manager;
    }
}
