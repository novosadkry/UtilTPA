package cz.novosadkry.UtilTPA.Commands.Back;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;

public class BackPlayerQuitEvent implements Listener {
    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent e) {
        BackPersist.lastLoc.remove(e.getPlayer());
    }
}
