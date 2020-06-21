package cz.novosadkry.UtilTPA.UI;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.SkullMeta;

import java.util.Collection;

public class RequestInventory {
    public static final String inventoryName = "Seznam hráčů";
    public static final int pageSize = 6 * 9;
    public static final int pagePlayerCount = pageSize - 9;

    public static Inventory createInventory(int page) {
        Collection<? extends Player> players = Bukkit.getOnlinePlayers();

        int startIndex = page * pageSize;

        if (startIndex >= players.size())
            return null;

        Inventory inventory = Bukkit.createInventory(new RequestInventoryHolder(), pageSize, inventoryName);
        players.stream().skip(startIndex).limit(pagePlayerCount).forEach(player -> {
            ItemStack item = new ItemStack(Material.PLAYER_HEAD);

            SkullMeta meta = (SkullMeta)item.getItemMeta();
            meta.setOwningPlayer(player);

            item.setItemMeta(meta);
            inventory.addItem(item);
        });

        return inventory;
    }
}
