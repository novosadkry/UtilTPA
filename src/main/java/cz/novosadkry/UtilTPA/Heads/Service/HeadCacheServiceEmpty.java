package cz.novosadkry.UtilTPA.Heads.Service;

import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class HeadCacheServiceEmpty implements HeadCacheService {
    @Override
    public ItemStack getHead(Player player) {
        return null;
    }

    @Override
    public void enqueueHead(Player player) {

    }

    @Override
    public void initialize() {

    }

    @Override
    public void terminate() {

    }
}
