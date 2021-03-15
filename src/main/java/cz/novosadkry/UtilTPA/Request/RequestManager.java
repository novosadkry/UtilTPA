package cz.novosadkry.UtilTPA.Request;

import cz.novosadkry.UtilTPA.BungeeCord.Drivers.BungeeDriver;
import cz.novosadkry.UtilTPA.BungeeCord.Transport.Messages.Concrete.RequestAcceptMessage;
import cz.novosadkry.UtilTPA.BungeeCord.Transport.Messages.Concrete.RequestDenyMessage;
import cz.novosadkry.UtilTPA.BungeeCord.Transport.Messages.Concrete.RequestMessage;
import cz.novosadkry.UtilTPA.Commands.Back.BackInfo;
import cz.novosadkry.UtilTPA.Commands.Back.BackPersist;
import cz.novosadkry.UtilTPA.Main;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.ComponentBuilder;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.chat.hover.content.Text;

import java.util.*;

public class RequestManager {
    private static RequestManager manager;
    private final Map<RequestPlayer, LinkedList<Request>> requests = new HashMap<>();
    private final Map<RequestPlayer, LinkedList<Request>> awaitedRequests = new HashMap<>();

    public Map<RequestPlayer, LinkedList<Request>> getAll() {
        return requests;
    }

    public LinkedList<Request> getAllPlayer(RequestPlayer player) {
        requests.computeIfAbsent(player, k -> new LinkedList<>());
        return requests.get(player);
    }

    public Map<RequestPlayer, LinkedList<Request>> getAwaited() {
        return awaitedRequests;
    }

    public LinkedList<Request> getAwaitedPlayer(RequestPlayer player) {
        awaitedRequests.computeIfAbsent(player, k -> new LinkedList<>());
        return awaitedRequests.get(player);
    }

    public Request get(RequestPlayer to) {
        return getAllPlayer(to).peek();
    }

    public Request getFrom(RequestPlayer to, RequestPlayer from) {
        return getAllPlayer(to).stream()
                .filter(r -> r.getFrom().equals(from))
                .findFirst().orElse(null);
    }

    public boolean hasRequests(RequestPlayer to) {
        return !getAllPlayer(to).isEmpty();
    }

    public boolean hasRequest(RequestPlayer to, Request request) {
        return getAllPlayer(to).contains(request);
    }

    public void sendRequest(Request request) {
        final RequestPlayer from = request.getFrom();
        final RequestPlayer to = request.getTo();

        BungeeDriver bungeeDriver = Main.getInstance().getBungeeDriver();

        if (!request.valid()) {
            from.onLocal(p -> p.sendMessage("§cPožadavek je neplatný!"));
            return;
        }

        if (hasRequest(to, request))
        {
            from.onRemote(p -> new RequestDenyMessage(request)
                    .setReason("§cTomuhle hráči už jsi request poslal!")
                    .send(Main.getInstance().getBungeeDriver()));

            from.onLocal(p -> p.sendMessage("§cTomuhle hráči už jsi request poslal!"));
            return;
        }

        if (to.isRemote()) {
            if (bungeeDriver.getPlayerList().contains(to.getName())) {
                new RequestMessage(request)
                        .send(Main.getInstance().getBungeeDriver());

                from.onLocal(p -> p.sendMessage("§bPoslal jsi teleport request hráčovi §e" + to));
            } else {
                from.onLocal(p -> p.sendMessage("§cHráč neexistuje nebo je offline!"));
            }
        }

        else {
            getAllPlayer(to).add(request);
            request.startCountdown();

            TextComponent tpAccept = new TextComponent("§a/tpaccept");
            TextComponent tpDeny = new TextComponent("§4/tpdeny");

            tpAccept.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/tpaccept " + from));
            tpAccept.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new Text("Kliknutím příjmeš request")));
            tpDeny.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/tpdeny  " + from));
            tpDeny.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new Text("Kliknutím odmítneš request")));

            ComponentBuilder targetMsg = new ComponentBuilder("\n§bByl ti poslán teleport request od hráče ")
                    .append("§e" + from)
                    .append("\n§bNa odpověd' máš §e20 §bsekund")
                    .append("\n\n§bPro příjmutí napiš ")
                    .append(tpAccept)
                    .append("\n§bPro odmítnutí napiš ")
                    .append(tpDeny);

            to.onLocal(p -> p.spigot().sendMessage(targetMsg.create()));
            from.onLocal(p -> p.sendMessage("§bPoslal jsi teleport request hráčovi §e" + to));
        }
    }

    public void timeoutRequest(Request request) {
        final RequestPlayer from = request.getFrom();
        final RequestPlayer to = request.getTo();

        getAllPlayer(to).remove(request);
        getAwaitedPlayer(from).remove(request);

        from.onLocal(p -> p.sendMessage("§cHráč §e"+ to +"§c neodpověděl na tvůj request."));
        to.onLocal(p -> p.sendMessage("§cNeodpověděl jsi na request hráče §e"+ from));

        from.onRemote(p -> new RequestDenyMessage(request)
                .setReason("§cHráč §e"+ to +"§c neodpověděl na tvůj request.")
                .send(Main.getInstance().getBungeeDriver()));
    }

    public void acceptRequest(Request request) {
        final RequestPlayer from = request.getFrom();
        final RequestPlayer to = request.getTo();

        getAllPlayer(to).remove(request);
        BungeeDriver bungeeDriver = Main.getInstance().getBungeeDriver();

        from.onLocal(p -> {
            BackPersist.getLastLoc().put(p, new BackInfo(p.getLocation()));

            p.teleport(to.getPlayer().getLocation());
            p.sendMessage("§aHráč §e" + to + " §apřijal tvůj request.");
        });

        from.onRemote(p -> {
            getAwaitedPlayer(from).add(request);

            new RequestAcceptMessage(request)
                    .setServer(bungeeDriver.getServerName())
                    .send(Main.getInstance().getBungeeDriver());
        });

        to.onLocal(p -> p.sendMessage("§aPřijal jsi request hráče §e" + from));
        request.cancelCountdown();
    }

    public void denyRequest(Request request) {
        final RequestPlayer from = request.getFrom();
        final RequestPlayer to = request.getTo();

        getAllPlayer(to).remove(request);

        from.onLocal(p -> p.sendMessage("§cHráč §e" + to + " §codmítnul tvůj request."));
        to.onLocal(p -> p.sendMessage("§cOdmítnul jsi request hráče §e" + from));

        from.onRemote(p -> new RequestDenyMessage(request)
                .setReason("§cHráč §e" + to + " §codmítnul tvůj request.")
                .send(Main.getInstance().getBungeeDriver()));

        request.cancelCountdown();
    }

    public static RequestManager getInstance() {
        if (manager == null)
            manager = new RequestManager();

        return manager;
    }
}
