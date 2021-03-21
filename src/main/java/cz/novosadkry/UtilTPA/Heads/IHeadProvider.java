package cz.novosadkry.UtilTPA.Heads;

import cz.novosadkry.UtilTPA.Services.IService;
import org.bukkit.inventory.ItemStack;

public interface IHeadProvider extends IService {
    ItemStack getHead(String player);
}
