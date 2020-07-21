package cz.novosadkry.UtilTPA.Heads;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

public class HeadCachePlayerJoinEvent implements Listener {
    @EventHandler
    public void onPlayerJoinEvent(PlayerJoinEvent e) {
        HeadCacheService cacheService = HeadCacheService.getInstance();
        cacheService.enqueueHead(e.getPlayer());
    }
}
