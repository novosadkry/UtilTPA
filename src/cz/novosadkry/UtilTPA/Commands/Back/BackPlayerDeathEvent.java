package cz.novosadkry.UtilTPA.Commands.Back;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;

public class BackPlayerDeathEvent implements Listener {
    @EventHandler
    public void onPlayerDeath(PlayerDeathEvent e) {
        BackPersist.lastLoc.put(e.getEntity(), e.getEntity().getLocation());
    }
}
