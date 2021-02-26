package cz.novosadkry.UtilTPA.Request;

import cz.novosadkry.UtilTPA.BungeeCord.Transport.Messages.RequestDenyMessage;
import cz.novosadkry.UtilTPA.BungeeCord.Transport.Messages.RequestMessage;
import cz.novosadkry.UtilTPA.Commands.Back.BackPersist;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.ComponentBuilder;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.TextComponent;

import java.util.Map;
import java.util.HashMap;
import java.util.LinkedList;

public class RequestManager {
    private static RequestManager manager;
    private final Map<RequestPlayer, LinkedList<Request>> requests = new HashMap<>();

    public Map<RequestPlayer, LinkedList<Request>> getAll() {
        return requests;
    }

    public LinkedList<Request> getAllPlayer(RequestPlayer player) {
        return requests.get(player);
    }

    public Request get(RequestPlayer to) {
        return requests.get(to).peek();
    }

    public Request getFrom(RequestPlayer to, RequestPlayer from) {
        return requests.get(to).stream()
                .filter(r -> r.getFrom().equals(from))
                .findFirst().orElse(null);
    }

    public boolean hasRequests(RequestPlayer to) {
        return requests.containsKey(to) && !requests.get(to).isEmpty();
    }

    public boolean hasRequest(RequestPlayer to, Request request) {
        return requests.containsKey(to) && requests.get(to).contains(request);
    }

    public void sendRequest(Request request) {
        getAll().computeIfAbsent(request.getTo(), k -> new LinkedList<>());

        final RequestPlayer from = request.getFrom();
        final RequestPlayer to = request.getTo();

        if (!request.valid()) {
            from.onLocal(p -> p.sendMessage("§cPožadavek je neplatný!"));
            return;
        }

        if (hasRequest(request.getTo(), request))
        {
            from.onRemote(p ->
                    new RequestDenyMessage(request)
                            .setReason("§cTomuhle hráči už jsi request poslal!")
                            .send()
            );

            from.onLocal(p -> p.sendMessage("§cTomuhle hráči už jsi request poslal!"));
            return;
        }

        // Player is probably on another server
        if (to.isRemote()) {
            // TODO: Send message based on player list
            new RequestMessage(request).send();
        }

        else {
            getAllPlayer(to).add(request);
            request.startCountdown();

            TextComponent tpAccept = new TextComponent("§a/tpaccept");
            TextComponent tpDeny = new TextComponent("§4/tpdeny");

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

            to.onLocal(p -> p.spigot().sendMessage(targetMsg.create()));
            from.onLocal(p -> p.sendMessage("§bPoslal si teleport request hráčovi §e" + request.getTo()));
        }
    }

    public void timeoutRequest(Request request) {
        getAllPlayer(request.getTo()).remove(request);

        final RequestPlayer from = request.getFrom();
        final RequestPlayer to = request.getTo();

        from.onLocal(p -> p.sendMessage("§cHráč §e"+ request.getTo() +"§c neodpověděl na tvůj request."));
        to.onLocal(p -> p.sendMessage("§cNeodpověděl si na request hráče §e"+ request.getFrom()));
    }

    public void acceptRequest(Request request) {
        getAllPlayer(request.getTo()).remove(request);

        final RequestPlayer from = request.getFrom();
        final RequestPlayer to = request.getTo();

        from.onLocal(p -> {
            BackPersist.lastLoc.put(p, p.getLocation());

            p.teleport(to.getPlayer().getLocation());
            p.sendMessage("§aHráč §e" + request.getTo() + " §apřijal tvůj request.");
        });

        to.onLocal(p -> p.sendMessage("§aPřijal jsi request hráče §e" + request.getFrom()));
        request.cancelCountdown();
    }

    public void denyRequest(Request request) {
        getAllPlayer(request.getTo()).remove(request);

        final RequestPlayer from = request.getFrom();
        final RequestPlayer to = request.getTo();

        from.onLocal(p -> p.sendMessage("§cHráč §e" + request.getTo() + " §codmítnul tvůj request."));
        to.onLocal(p -> p.sendMessage("§cOdmítnul jsi request hráče §e" + request.getFrom()));

        request.cancelCountdown();
    }

    public static RequestManager getInstance() {
        if (manager == null)
            manager = new RequestManager();

        return manager;
    }
}
