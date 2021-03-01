package cz.novosadkry.UtilTPA.Commands.Back;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;

public class BackPlayerDeathListener implements Listener {
    @EventHandler
    public void onPlayerDeath(PlayerDeathEvent e) {
        BackPersist.getLastLoc().put(e.getEntity(), new BackInfo(e.getEntity().getLocation()));
    }
}
