package cz.novosadkry.UtilTPA.Heads.Service;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.SkullMeta;

public class HeadCacheServiceOffline implements HeadCacheService {
    @Override
    public ItemStack getHead(Player player) {
        Material mat = null;

        switch (player.getUniqueId().hashCode() % 4) {
            case 0:
                mat = Material.SKELETON_SKULL;
                break;
            case 1:
                mat = Material.CREEPER_HEAD;
                break;
            case 2:
                mat = Material.ZOMBIE_HEAD;
                break;
            case 3:
                mat = Material.WITHER_SKELETON_SKULL;
                break;
            default:
                mat = Material.PLAYER_HEAD;
        }

        ItemStack skullItem = new ItemStack(mat);

        SkullMeta skullMeta = (SkullMeta)skullItem.getItemMeta();
        skullMeta.setDisplayName(player.getName());

        skullItem.setItemMeta(skullMeta);
        return skullItem;
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
