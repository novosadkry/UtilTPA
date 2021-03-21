package cz.novosadkry.UtilTPA.Heads.Listeners;

import cz.novosadkry.UtilTPA.Heads.Service.IHeadCacheService;
import cz.novosadkry.UtilTPA.Main;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

public class HeadPlayerJoinListener implements Listener {
    @EventHandler
    public void onPlayerJoinEvent(PlayerJoinEvent e) {
        IHeadCacheService headCache = Main.getService(IHeadCacheService.class);

        if (headCache != null)
            headCache.enqueueHead(e.getPlayer().getName());
    }
}
