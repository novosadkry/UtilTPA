package cz.novosadkry.UtilTPA.Heads;

import org.bukkit.inventory.ItemStack;

public interface IHeadProvider {
    ItemStack getHead(String player);

    void initialize();

    void terminate();
}
