package cz.novosadkry.UtilTPA.Heads;

import cz.novosadkry.UtilTPA.Main;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

public class HeadCachePlayerJoinEvent implements Listener {
    @EventHandler
    public void onPlayerJoinEvent(PlayerJoinEvent e) {
        HeadCacheService cacheService = Main.getInstance().getHeadCacheService();
        cacheService.enqueueHead(e.getPlayer());
    }
}
