package cz.novosadkry.UtilTPA.UI;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.SkullMeta;

public class RequestInventoryClickListener implements Listener {
    @EventHandler
    public void onClick(InventoryClickEvent e) {
        if (e.getInventory().getHolder() instanceof RequestInventoryHolder) {
            RequestInventoryHolder holder = (RequestInventoryHolder) e.getInventory().getHolder();

            e.setCancelled(true);

            if (e.getCurrentItem() == null || !e.getCurrentItem().hasItemMeta())
                return;

            ItemMeta meta = e.getCurrentItem().getItemMeta();
            if (meta instanceof SkullMeta) {
                SkullMeta skullMeta = (SkullMeta)meta;

                String target = skullMeta.getDisplayName();
                Player sender = (Player)e.getWhoClicked();

                sender.performCommand("tpa " + target);

                e.getWhoClicked().closeInventory();
            }

            else {
                RequestInventory requestInventory = holder.getRequestInventory();
                int currentPage = requestInventory.getCurrentPage();

                // Next arrow
                if (e.getSlot() == e.getInventory().getSize() - 4) {
                    requestInventory.setCurrentPage(currentPage + 1);
                    requestInventory.updateInventory();
                }

                // Page refresh
                if (e.getSlot() == e.getInventory().getSize() - 5) {
                    requestInventory.setCurrentPage(currentPage);
                    requestInventory.updateInventory();
                }

                // Back arrow
                else if (e.getSlot() == e.getInventory().getSize() - 6) {
                    requestInventory.setCurrentPage(currentPage - 1);
                    requestInventory.updateInventory();
                }
            }
        }
    }
}
