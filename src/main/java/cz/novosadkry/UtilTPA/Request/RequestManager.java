package cz.novosadkry.UtilTPA.Request;

import cz.novosadkry.UtilTPA.BungeeCord.Drivers.BungeeDriver;
import cz.novosadkry.UtilTPA.BungeeCord.Transport.Messages.Concrete.RequestAcceptMessage;
import cz.novosadkry.UtilTPA.BungeeCord.Transport.Messages.Concrete.RequestDenyMessage;
import cz.novosadkry.UtilTPA.BungeeCord.Transport.Messages.Concrete.RequestMessage;
import cz.novosadkry.UtilTPA.Commands.Back.BackInfo;
import cz.novosadkry.UtilTPA.Commands.Back.BackPersist;
import cz.novosadkry.UtilTPA.Localization.PlaceHolder;
import cz.novosadkry.UtilTPA.Main;
import net.md_5.bungee.api.chat.*;
import net.md_5.bungee.api.chat.hover.content.Text;

import java.util.*;

import static cz.novosadkry.UtilTPA.Localization.Locale.*;

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
            from.onLocal(p -> p.sendMessage(tl("requests.error.invalid")));
            return;
        }

        if (hasRequest(to, request))
        {
            from.onRemote(p -> new RequestDenyMessage(request)
                    .setReason(tl("requests.error.alreadySent"))
                    .send(Main.getInstance().getBungeeDriver()));

            from.onLocal(p -> p.sendMessage(tl("requests.error.alreadySent")));
            return;
        }

        if (to.isRemote()) {
            if (bungeeDriver.getPlayerList().contains(to.getName())) {
                new RequestMessage(request)
                        .send(Main.getInstance().getBungeeDriver());

                from.onLocal(p -> p.sendMessage(tl("requests.send.from", new PlaceHolder("player", to))));
            } else {
                from.onLocal(p -> p.sendMessage(tl("requests.error.invalid")));
            }
        }

        else {
            getAllPlayer(to).add(request);
            request.startCountdown();

            TextComponent tpAccept = new TextComponent("§a/tpaccept");
            TextComponent tpDeny = new TextComponent("§4/tpdeny");

            tpAccept.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/tpaccept " + from));
            tpAccept.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new Text(tl("requests.hover.accept"))));
            tpDeny.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/tpdeny  " + from));
            tpDeny.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new Text(tl("requests.hover.deny"))));

            BaseComponent[] msg = tlc("requests.send.to",
                    new PlaceHolder("player", from),
                    new PlaceHolder("timeout", "20"),
                    new PlaceHolder("tpaccept", tpAccept),
                    new PlaceHolder("tpdeny", tpDeny)
            );

            to.onLocal(p -> p.spigot().sendMessage(msg));
            from.onLocal(p -> p.sendMessage(tl("requests.send.from", new PlaceHolder("player", to))));
        }
    }

    public void timeoutRequest(Request request) {
        final RequestPlayer from = request.getFrom();
        final RequestPlayer to = request.getTo();

        getAllPlayer(to).remove(request);
        getAwaitedPlayer(from).remove(request);

        from.onLocal(p -> p.sendMessage(tl("requests.timeout.from", new PlaceHolder("player", to))));
        to.onLocal(p -> p.sendMessage(tl("requests.timeout.to", new PlaceHolder("player", from))));

        from.onRemote(p -> new RequestDenyMessage(request)
                .setReason(tl("requests.timeout.from", new PlaceHolder("player", to)))
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
            p.sendMessage(tl("requests.accept.from", new PlaceHolder("player", to)));
        });

        from.onRemote(p -> {
            getAwaitedPlayer(from).add(request);

            new RequestAcceptMessage(request)
                    .setServer(bungeeDriver.getServerName())
                    .send(Main.getInstance().getBungeeDriver());
        });

        to.onLocal(p -> p.sendMessage(tl("requests.accept.to", new PlaceHolder("player", from))));
        request.cancelCountdown();
    }

    public void denyRequest(Request request) {
        final RequestPlayer from = request.getFrom();
        final RequestPlayer to = request.getTo();

        getAllPlayer(to).remove(request);

        from.onLocal(p -> p.sendMessage(tl("requests.deny.from", new PlaceHolder("player", to))));
        to.onLocal(p -> p.sendMessage(tl("requests.deny.to", new PlaceHolder("player", from))));

        from.onRemote(p -> new RequestDenyMessage(request)
                .setReason(tl("requests.deny.from", new PlaceHolder("player", to)))
                .send(Main.getInstance().getBungeeDriver()));

        request.cancelCountdown();
    }

    public static RequestManager getInstance() {
        if (manager == null)
            manager = new RequestManager();

        return manager;
    }
}
