package cz.novosadkry.UtilTPA.Heads.Service;

import org.bukkit.inventory.ItemStack;

public interface HeadCacheService {
    ItemStack getHead(String player);

    void enqueueHead(String player);

    void initialize();

    void terminate();
}
