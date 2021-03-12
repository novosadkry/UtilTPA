package cz.novosadkry.UtilTPA.UI;

import cz.novosadkry.UtilTPA.BungeeCord.Drivers.BungeeDriver;
import cz.novosadkry.UtilTPA.Heads.Service.HeadCacheService;
import cz.novosadkry.UtilTPA.Main;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class RequestInventory {
    private Inventory inventory;
    private final String inventoryName;
    private final Player owner;

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
        BungeeDriver bungeeDriver = Main.getInstance().getBungeeDriver();

        return (int)((
            Math.ceil(
                Math.min(
                    (float)(bungeeDriver.getPlayerList().size() - 1) / 9,
                    5
                )
            ) + 1) * 9
        );
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
        BungeeDriver bungeeDriver = Main.getInstance().getBungeeDriver();

        String[] players = bungeeDriver.getPlayerList().stream()
                .filter(p -> !p.equals(owner.getName()))
                .toArray(String[]::new);

        if (getPageSizeAdaptive() != getPageSize())
            resizeAdaptive();

        int startIndex = page * getPagePlayerCount();
        int maxPlayerCount = Math.min(players.length - startIndex, getPagePlayerCount());

        setContent(players, startIndex, maxPlayerCount);
        setFooter(page, players.length, startIndex);

        currentPage = page;
    }

    private void setContent(String[] players, int startIndex, int maxPlayerCount) {
        HeadCacheService cacheService = Main.getInstance().getHeadCacheService();
        ItemStack[] contents = new ItemStack[getPagePlayerCount()];

        for (int i = 0; i < maxPlayerCount; i++) {
            String player = players[startIndex + i];

            ItemStack skullItem = cacheService.getHead(player);
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
