package cz.novosadkry.UtilTPA;

import cz.novosadkry.UtilTPA.BungeeCord.BungeeDriver;
import cz.novosadkry.UtilTPA.BungeeCord.Transport.Listeners.PingMessageListener;
import cz.novosadkry.UtilTPA.BungeeCord.Transport.Listeners.PlayerListMessageListener;
import cz.novosadkry.UtilTPA.BungeeCord.Transport.Listeners.RequestMessageListener;
import cz.novosadkry.UtilTPA.Commands.Back.BackExecutor;
import cz.novosadkry.UtilTPA.Commands.Back.BackPlayerDeathEvent;
import cz.novosadkry.UtilTPA.Commands.Back.BackPlayerQuitEvent;
import cz.novosadkry.UtilTPA.Commands.TPA.TpAcceptExecutor;
import cz.novosadkry.UtilTPA.Commands.TPA.TpDenyExecutor;
import cz.novosadkry.UtilTPA.Commands.TPA.TpaExecutor;
import cz.novosadkry.UtilTPA.Heads.HeadCachePlayerJoinEvent;
import cz.novosadkry.UtilTPA.Heads.HeadCacheService;
import cz.novosadkry.UtilTPA.UI.RequestInventoryClickEvent;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.event.HandlerList;
import org.bukkit.plugin.java.JavaPlugin;

public class Main extends JavaPlugin {
    public static FileConfiguration config;
    public static HeadCacheService headCacheService;

    private static Main instance;

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
        this.getCommand("back").setExecutor(new BackExecutor());

        this.getServer().getPluginManager().registerEvents(new RequestInventoryClickEvent(), this);
        this.getServer().getPluginManager().registerEvents(new HeadCachePlayerJoinEvent(), this);
        this.getServer().getPluginManager().registerEvents(new BackPlayerDeathEvent(), this);
        this.getServer().getPluginManager().registerEvents(new BackPlayerQuitEvent(), this);

        this.getServer().getMessenger().registerIncomingPluginChannel(this, "BungeeCord", BungeeDriver.getInstance());
        this.getServer().getMessenger().registerOutgoingPluginChannel(this, "BungeeCord");

        BungeeDriver.getInstance().registerListener(new PingMessageListener());
        BungeeDriver.getInstance().registerListener(new RequestMessageListener());
        BungeeDriver.getInstance().registerListener(new PlayerListMessageListener());

        if (headCacheService != null) {
            headCacheService.startCacheQueue();
            headCacheService.startCacheRefresh();
        }

        instance = this;
        super.onEnable();
    }

    @Override
    public void onDisable() {
        HandlerList.unregisterAll(this);

        BungeeDriver.getInstance().unregisterListeners();

        headCacheService.stopCacheQueue();
        headCacheService.stopCacheRefresh();

        super.onDisable();
    }

    public static Main getInstance() {
        return instance;
    }
}
