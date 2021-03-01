package cz.novosadkry.UtilTPA.Heads.Service;

import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public interface HeadCacheService {
    ItemStack getHead(Player player);

    void enqueueHead(Player player);

    void initialize();

    void terminate();
}
