package cz.novosadkry.UtilTPA;

import cz.novosadkry.UtilTPA.UI.RequestInventoryClickEvent;
import org.bukkit.plugin.java.JavaPlugin;

public class Main extends JavaPlugin {
    @Override
    public void onEnable() {
        this.getCommand("tpa").setExecutor(new TpaExecutor());
        this.getCommand("tpaccept").setExecutor(new TpAcceptExecutor());
        this.getCommand("tpdeny").setExecutor(new TpDenyExecutor());

        this.getServer().getPluginManager().registerEvents(new RequestInventoryClickEvent(), this);
    }
}
