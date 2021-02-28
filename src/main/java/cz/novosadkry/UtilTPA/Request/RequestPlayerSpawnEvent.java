package cz.novosadkry.UtilTPA.Request;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.spigotmc.event.player.PlayerSpawnLocationEvent;

import java.util.LinkedList;

public class RequestPlayerSpawnEvent implements Listener {
    @EventHandler
    public void onPlayerSpawnEvent(PlayerSpawnLocationEvent e) {
        RequestPlayer requestPlayer = new RequestPlayer(e.getPlayer());
        RequestManager requestManager = RequestManager.getInstance();

        LinkedList<Request> awaited = requestManager.getAwaitedPlayer(requestPlayer);
        Request request = awaited.peekLast();

        if (request != null) {
            Player from = e.getPlayer();

            request.getTo().onLocal(to -> {
                e.setSpawnLocation(to.getLocation());
                from.sendMessage("§aHráč §e" + to + " §apřijal tvůj request.");

                awaited.clear();
            });
        }
    }
}