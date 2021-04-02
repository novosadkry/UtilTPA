package cz.novosadkry.UtilTPA.Requests.Listeners;

import cz.novosadkry.UtilTPA.Localization.PlaceHolder;
import cz.novosadkry.UtilTPA.Requests.ExpiringRequest;
import cz.novosadkry.UtilTPA.Requests.Managers.RequestManager;
import cz.novosadkry.UtilTPA.Requests.RequestPlayer;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.spigotmc.event.player.PlayerSpawnLocationEvent;

import java.util.LinkedList;

import static cz.novosadkry.UtilTPA.Localization.Locale.*;

public class RequestPlayerSpawnListener implements Listener {
    @EventHandler
    public void onPlayerSpawnEvent(PlayerSpawnLocationEvent e) {
        RequestPlayer requestPlayer = new RequestPlayer(e.getPlayer());
        RequestManager requestManager = RequestManager.getInstance();

        LinkedList<ExpiringRequest> awaited = requestManager.getAwaitedPlayer(requestPlayer);
        ExpiringRequest request = awaited.peekLast();

        if (request != null) {
            Player from = e.getPlayer();

            request.getTo().onLocal(to -> {
                e.setSpawnLocation(to.getLocation());
                from.sendMessage(tl("requests.accept.from", new PlaceHolder("player", to)));

                awaited.clear();
            });

            request.onResolved();
        }
    }
}
