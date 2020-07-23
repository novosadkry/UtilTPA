package cz.novosadkry.UtilTPA;

import cz.novosadkry.UtilTPA.Heads.HeadCachePlayerJoinEvent;
import cz.novosadkry.UtilTPA.Heads.HeadCacheService;
import cz.novosadkry.UtilTPA.UI.RequestInventoryClickEvent;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

public class Main extends JavaPlugin {
    public static FileConfiguration config;
    public static HeadCacheService headCacheService;

    @Override
    public void onEnable() {
        this.saveDefaultConfig();
        config = this.getConfig();

        if (config.getBoolean("online-mode")) {
            headCacheService = new HeadCacheService(
                    config.getLong("cache.expire"),
                    config.getLong("cache.refresh"),
                    config.getLong("cache.queue")
            );
        }

        this.getCommand("tpa").setExecutor(new TpaExecutor());
        this.getCommand("tpaccept").setExecutor(new TpAcceptExecutor());
        this.getCommand("tpdeny").setExecutor(new TpDenyExecutor());

        this.getServer().getPluginManager().registerEvents(new RequestInventoryClickEvent(), this);
        this.getServer().getPluginManager().registerEvents(new HeadCachePlayerJoinEvent(), this);

        if (headCacheService != null) {
            headCacheService.startCacheQueue();
            headCacheService.startCacheRefresh();
        }
    }
}
