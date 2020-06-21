package cz.novosadkry.UtilTPA.UI;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.SkullMeta;

public class RequestInventory {
    private Inventory inventory;
    private Player owner;

    private int currentPage;

    private final int pageSize;
    private final int pagePlayerCount;

    public RequestInventory(Player owner) {
        this(owner, 6 * 9);
    }

    public RequestInventory(Player owner, int pageSize) {
        this(owner, pageSize, "Seznam hráčů");
    }

    public RequestInventory(Player owner, int pageSize, String inventoryName) {
        this.owner = owner;
        this.pageSize = pageSize;
        this.pagePlayerCount = pageSize - 9;

        RequestInventoryHolder holder = new RequestInventoryHolder(this);
        this.inventory = Bukkit.createInventory(holder, pageSize, inventoryName);

        setCurrentPage(0);
    }

    public Inventory getInventory() {
        return inventory;
    }

    public Player getOwner() {
        return owner;
    }

    public int getCurrentPage() {
        return currentPage;
    }

    public void setCurrentPage(int page) {
        Player[] players = Bukkit.getOnlinePlayers()
                .stream()
                .filter(p -> p != owner)
                .toArray(Player[]::new);

        int startIndex = page * pagePlayerCount;
        int maxPlayerCount = Math.min(players.length - startIndex, pagePlayerCount);

        if (Bukkit.getServer().getOnlineMode())
            setOnlineContent(players, startIndex, maxPlayerCount);
        else
            setOfflineContent(players, startIndex, maxPlayerCount);

        setFooter(page, players.length, startIndex);
        currentPage = page;
    }

    private void setOnlineContent(Player[] players, int startIndex, int maxPlayerCount) {
        ItemStack[] contents = new ItemStack[pagePlayerCount];

        for (int i = 0; i < maxPlayerCount; i++) {
            Player player = players[startIndex + i];

            ItemStack skullItem = new ItemStack(Material.PLAYER_HEAD);

            SkullMeta skullMeta = (SkullMeta)skullItem.getItemMeta();
            skullMeta.setOwningPlayer(player);
            skullMeta.setDisplayName(player.getName());

            skullItem.setItemMeta(skullMeta);
            contents[i] = skullItem;
        }

        inventory.setContents(contents);
    }

    private void setOfflineContent(Player[] players, int startIndex, int maxPlayerCount) {
        ItemStack[] contents = new ItemStack[pagePlayerCount];

        for (int i = 0; i < maxPlayerCount; i++) {
            Player player = players[startIndex + i];
            Material mat = null;

            switch (player.hashCode() % 4) {
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
            contents[i] = skullItem;
        }

        inventory.setContents(contents);
    }

    private void setFooter(int page, int playerCount, int startIndex) {
        if (playerCount - startIndex > pagePlayerCount) {
            ItemStack nextItem = new ItemStack(Material.FEATHER);

            ItemMeta nextMeta = nextItem.getItemMeta();
            nextMeta.setDisplayName("Další");

            nextItem.setItemMeta(nextMeta);
            inventory.setItem(pageSize - 4, nextItem);
        }

        if (page > 0) {
            ItemStack backItem = new ItemStack(Material.FEATHER);

            ItemMeta backMeta = backItem.getItemMeta();
            backMeta.setDisplayName("Zpět");

            backItem.setItemMeta(backMeta);
            inventory.setItem(pageSize - 6, backItem);
        }

        ItemStack pageItem = new ItemStack(Material.BOOK);

        ItemMeta pageMeta = pageItem.getItemMeta();
        pageMeta.setDisplayName(String.format(
                "Strana %d z %d",
                page + 1,
                (int)Math.ceil((float)playerCount / pagePlayerCount))
        );

        pageItem.setItemMeta(pageMeta);
        inventory.setItem(pageSize - 5, pageItem);
    }
}
