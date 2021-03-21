package cz.novosadkry.UtilTPA.Heads.Listeners;

import cz.novosadkry.UtilTPA.Heads.IHeadProvider;
import cz.novosadkry.UtilTPA.Heads.Service.IHeadCacheService;
import cz.novosadkry.UtilTPA.Main;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

public class HeadPlayerJoinListener implements Listener {
    @EventHandler
    public void onPlayerJoinEvent(PlayerJoinEvent e) {
        IHeadProvider provider = Main.getInstance().getHeadProvider();

        if (provider instanceof IHeadCacheService)
            ((IHeadCacheService) provider).enqueueHead(e.getPlayer().getName());
    }
}
