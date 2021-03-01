package cz.novosadkry.UtilTPA.Heads.Cache;

import cz.novosadkry.UtilTPA.Heads.Service.HeadCacheService;
import cz.novosadkry.UtilTPA.Main;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

public class HeadCachePlayerJoinListener implements Listener {
    @EventHandler
    public void onPlayerJoinEvent(PlayerJoinEvent e) {
        HeadCacheService cacheService = Main.getInstance().getHeadCacheService();
        cacheService.enqueueHead(e.getPlayer());
    }
}
