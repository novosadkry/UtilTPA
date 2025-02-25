package cz.novosadkry.UtilTPA.Request;

import cz.novosadkry.UtilTPA.BungeeCord.Drivers.IBungeeDriver;
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
    private final Map<RequestPlayer, LinkedList<ExpiringRequest>> awaitedRequests = new HashMap<>();

    public Map<RequestPlayer, LinkedList<Request>> getAll() {
        return requests;
    }

    public LinkedList<Request> getAllPlayer(RequestPlayer player) {
        requests.computeIfAbsent(player, k -> new LinkedList<>());
        return requests.get(player);
    }

    public Map<RequestPlayer, LinkedList<ExpiringRequest>> getAwaited() {
        return awaitedRequests;
    }

    public LinkedList<ExpiringRequest> getAwaitedPlayer(RequestPlayer player) {
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

        IBungeeDriver bungeeDriver = Main.getService(IBungeeDriver.class);

        if (!request.valid()) {
            from.onLocal(p -> p.sendMessage(tl("requests.error.invalid")));
            return;
        }

        if (hasRequest(to, request))
        {
            from.onRemote(p -> new RequestDenyMessage(request)
                    .setReason(tl("requests.error.alreadySent"))
                    .send(Main.getService(IBungeeDriver.class)));

            from.onLocal(p -> p.sendMessage(tl("requests.error.alreadySent")));
            return;
        }

        if (to.isRemote()) {
            if (bungeeDriver.getPlayerList().contains(to.getName())) {
                new RequestMessage(request)
                        .send(Main.getService(IBungeeDriver.class));

                from.onLocal(p -> p.sendMessage(tl("requests.send.from", new PlaceHolder("player", to))));
            } else {
                from.onLocal(p -> p.sendMessage(tl("requests.error.invalid")));
            }
        }

        else {
            getAllPlayer(to).add(request);

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

            request.onSent();
        }
    }

    public void timeoutRequest(ExpiringRequest request) {
        final RequestPlayer from = request.getFrom();
        final RequestPlayer to = request.getTo();

        getAllPlayer(to).remove(request);
        getAwaitedPlayer(from).remove(request);

        from.onLocal(p -> p.sendMessage(tl("requests.timeout.from", new PlaceHolder("player", to))));
        to.onLocal(p -> p.sendMessage(tl("requests.timeout.to", new PlaceHolder("player", from))));

        from.onRemote(p -> new RequestDenyMessage(request)
                .setReason(tl("requests.timeout.from", new PlaceHolder("player", to)))
                .send(Main.getService(IBungeeDriver.class)));
    }

    public void acceptRequest(Request request) {
        final RequestPlayer from = request.getFrom();
        final RequestPlayer to = request.getTo();

        getAllPlayer(to).remove(request);
        IBungeeDriver bungeeDriver = Main.getService(IBungeeDriver.class);

        from.onLocal(p -> {
            BackPersist.getLastLoc().put(p, new BackInfo(p.getLocation()));

            p.teleport(to.getPlayer().getLocation());
            p.sendMessage(tl("requests.accept.from", new PlaceHolder("player", to)));
        });

        from.onRemote(p -> {
            getAwaitedPlayer(from).add(
                    new ExpiringRequest(request, 20000)
                            .startCountdown()
            );

            new RequestAcceptMessage(request)
                    .setServer(bungeeDriver.getServerName())
                    .send(Main.getService(IBungeeDriver.class));
        });

        to.onLocal(p -> p.sendMessage(tl("requests.accept.to", new PlaceHolder("player", from))));
        request.onResolved();
    }

    public void denyRequest(Request request) {
        final RequestPlayer from = request.getFrom();
        final RequestPlayer to = request.getTo();

        getAllPlayer(to).remove(request);

        from.onLocal(p -> p.sendMessage(tl("requests.deny.from", new PlaceHolder("player", to))));
        to.onLocal(p -> p.sendMessage(tl("requests.deny.to", new PlaceHolder("player", from))));

        from.onRemote(p -> new RequestDenyMessage(request)
                .setReason(tl("requests.deny.from", new PlaceHolder("player", to)))
                .send(Main.getService(IBungeeDriver.class)));

        request.onResolved();
    }

    public static RequestManager getInstance() {
        if (manager == null)
            manager = new RequestManager();

        return manager;
    }
}
