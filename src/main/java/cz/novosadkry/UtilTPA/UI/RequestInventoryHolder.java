package cz.novosadkry.UtilTPA.UI;

import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;

public class RequestInventoryHolder implements InventoryHolder {
    private RequestInventory requestInventory;

    public RequestInventoryHolder(RequestInventory requestInventory) {
        this.requestInventory = requestInventory;
    }

    public RequestInventory getRequestInventory() {
        return requestInventory;
    }

    @Override
    public Inventory getInventory() {
        return requestInventory.getInventory();
    }
}
