package cz.novosadkry.UtilTPA;

import cz.novosadkry.UtilTPA.BungeeCord.Drivers.BungeeDriver;
import cz.novosadkry.UtilTPA.BungeeCord.Drivers.BungeeDriverEmpty;
import cz.novosadkry.UtilTPA.BungeeCord.Drivers.BungeeDriverImpl;
import cz.novosadkry.UtilTPA.BungeeCord.Transport.Listeners.PingMessageListener;
import cz.novosadkry.UtilTPA.BungeeCord.Transport.Listeners.PlayerListMessageListener;
import cz.novosadkry.UtilTPA.BungeeCord.Transport.Listeners.RequestMessageListener;
import cz.novosadkry.UtilTPA.Commands.Back.BackExecutor;
import cz.novosadkry.UtilTPA.Commands.Back.BackPlayerDeathEvent;
import cz.novosadkry.UtilTPA.Commands.Back.BackPlayerQuitEvent;
import cz.novosadkry.UtilTPA.Commands.TPA.TpAcceptExecutor;
import cz.novosadkry.UtilTPA.Commands.TPA.TpDenyExecutor;
import cz.novosadkry.UtilTPA.Commands.TPA.TpaExecutor;
import cz.novosadkry.UtilTPA.Heads.Cache.HeadCachePlayerJoinEvent;
import cz.novosadkry.UtilTPA.Heads.Service.HeadCacheService;
import cz.novosadkry.UtilTPA.Heads.Service.HeadCacheServiceEmpty;
import cz.novosadkry.UtilTPA.Heads.Service.HeadCacheServiceImpl;
import cz.novosadkry.UtilTPA.Request.RequestPlayerSpawnEvent;
import cz.novosadkry.UtilTPA.UI.RequestInventoryClickEvent;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.event.HandlerList;
import org.bukkit.plugin.java.JavaPlugin;

public class Main extends JavaPlugin {
    private static Main instance;

    private HeadCacheService headCacheService;
    private BungeeDriver bungeeDriver;

    @Override
    public void onLoad() {
        instance = this;
        super.onLoad();
    }

    @Override
    public void onEnable() {
        FileConfiguration config = getConfig();
        saveDefaultConfig();

        headCacheService = config.getBoolean("online-mode")
                ? new HeadCacheServiceImpl(
                        config.getLong("cache.expire"),
                        config.getLong("cache.refresh"),
                        config.getLong("cache.queue"))
                : new HeadCacheServiceEmpty();

        bungeeDriver = config.getBoolean("bungeecord")
                ? new BungeeDriverImpl()
                : new BungeeDriverEmpty();

        getCommand("tpa").setExecutor(new TpaExecutor());
        getCommand("tpaccept").setExecutor(new TpAcceptExecutor());
        getCommand("tpdeny").setExecutor(new TpDenyExecutor());
        getCommand("back").setExecutor(new BackExecutor());

        getServer().getPluginManager().registerEvents(new RequestPlayerSpawnEvent(), this);
        getServer().getPluginManager().registerEvents(new RequestInventoryClickEvent(), this);
        getServer().getPluginManager().registerEvents(new HeadCachePlayerJoinEvent(), this);
        getServer().getPluginManager().registerEvents(new BackPlayerDeathEvent(), this);
        getServer().getPluginManager().registerEvents(new BackPlayerQuitEvent(), this);

        getBungeeDriver().initialize();
        getBungeeDriver().registerListener(new PingMessageListener());
        getBungeeDriver().registerListener(new RequestMessageListener());
        getBungeeDriver().registerListener(new PlayerListMessageListener());

        getHeadCacheService().startCacheQueue();
        getHeadCacheService().startCacheRefresh();

        super.onEnable();
    }

    @Override
    public void onDisable() {
        HandlerList.unregisterAll(this);

        getBungeeDriver().terminate();

        getHeadCacheService().stopCacheQueue();
        getHeadCacheService().stopCacheRefresh();

        super.onDisable();
    }

    public BungeeDriver getBungeeDriver() {
        return bungeeDriver;
    }

    public HeadCacheService getHeadCacheService() {
        return headCacheService;
    }

    public static Main getInstance() {
        return instance;
    }
}
