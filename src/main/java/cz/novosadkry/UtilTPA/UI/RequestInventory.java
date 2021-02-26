package cz.novosadkry.UtilTPA.UI;

import cz.novosadkry.UtilTPA.Heads.HeadCacheService;
import cz.novosadkry.UtilTPA.Main;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.SkullMeta;

public class RequestInventory {
    private Inventory inventory;
    private String inventoryName;

    private Player owner;

    private int currentPage;
    private boolean shouldReopen;

    public RequestInventory(Player owner) {
        this(owner, getPageSizeAdaptive());
    }

    public RequestInventory(Player owner, int pageSize) {
        this(owner, pageSize, "Seznam hráčů");
    }

    public RequestInventory(Player owner, int pageSize, String inventoryName) {
        this.owner = owner;
        this.inventory = createInventory(pageSize, inventoryName);
        this.inventoryName = inventoryName;

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

    public int getPageSize() {
        return inventory.getSize();
    }

    public int getPagePlayerCount(){
        return inventory.getSize() - 9;
    }

    public static int getPageSizeAdaptive() {
        return (int)((
            Math.ceil(
                Math.min(
                    (float)(Bukkit.getOnlinePlayers().size() - 1) / 9,
                    5
                )
            ) + 1) * 9);
    }

    public void resizeAdaptive() {
        inventory = createInventory(getPageSizeAdaptive(), inventoryName);
        shouldReopen = true;
    }

    public void openInventory() {
        owner.openInventory(inventory);
        shouldReopen = false;
    }

    public void updateInventory() {
        if (shouldReopen)
            openInventory();
        else
            owner.updateInventory();
    }

    public void setCurrentPage(int page) {
        Player[] players = Bukkit.getOnlinePlayers()
                .stream()
                .filter(p -> p != owner)
                .toArray(Player[]::new);

        if (getPageSizeAdaptive() != getPageSize())
            resizeAdaptive();

        int startIndex = page * getPagePlayerCount();
        int maxPlayerCount = Math.min(players.length - startIndex, getPagePlayerCount());

        if (Main.config.getBoolean("online-mode"))
            setOnlineContent(players, startIndex, maxPlayerCount);
        else
            setOfflineContent(players, startIndex, maxPlayerCount);

        setFooter(page, players.length, startIndex);
        currentPage = page;
    }

    private void setOnlineContent(Player[] players, int startIndex, int maxPlayerCount) {
        HeadCacheService cacheService = Main.headCacheService;
        ItemStack[] contents = new ItemStack[getPagePlayerCount()];

        for (int i = 0; i < maxPlayerCount; i++) {
            Player player = players[startIndex + i];

            ItemStack skullItem = cacheService.getHead(player);
            contents[i] = skullItem;
        }

        inventory.setContents(contents);
    }

    private void setOfflineContent(Player[] players, int startIndex, int maxPlayerCount) {
        ItemStack[] contents = new ItemStack[getPagePlayerCount()];

        for (int i = 0; i < maxPlayerCount; i++) {
            Player player = players[startIndex + i];
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
            contents[i] = skullItem;
        }

        inventory.setContents(contents);
    }

    private void setFooter(int page, int playerCount, int startIndex) {
        if (playerCount - startIndex > getPagePlayerCount()) {
            ItemStack nextItem = new ItemStack(Material.FEATHER);

            ItemMeta nextMeta = nextItem.getItemMeta();
            nextMeta.setDisplayName("Další");

            nextItem.setItemMeta(nextMeta);
            inventory.setItem(getPageSize() - 4, nextItem);
        }

        if (page > 0) {
            ItemStack backItem = new ItemStack(Material.FEATHER);

            ItemMeta backMeta = backItem.getItemMeta();
            backMeta.setDisplayName("Zpět");

            backItem.setItemMeta(backMeta);
            inventory.setItem(getPageSize() - 6, backItem);
        }

        ItemStack pageItem = new ItemStack(Material.BOOK);

        ItemMeta pageMeta = pageItem.getItemMeta();
        pageMeta.setDisplayName(String.format(
                "Strana %d z %d",
                page + 1,
                (int)Math.ceil((float)playerCount / getPagePlayerCount()))
        );

        pageItem.setItemMeta(pageMeta);
        inventory.setItem(getPageSize() - 5, pageItem);
    }

    private Inventory createInventory(int pageSize, String inventoryName) {
        RequestInventoryHolder holder = new RequestInventoryHolder(this);
        return Bukkit.createInventory(holder, pageSize, inventoryName);
    }
}
