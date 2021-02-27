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
import cz.novosadkry.UtilTPA.Heads.HeadCachePlayerJoinEvent;
import cz.novosadkry.UtilTPA.Heads.HeadCacheService;
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

        if (config.getBoolean("online-mode")) {
            headCacheService = new HeadCacheService(
                    config.getLong("cache.expire"),
                    config.getLong("cache.refresh"),
                    config.getLong("cache.queue")
            );
        }

        bungeeDriver = config.getBoolean("bungeecord")
                ? new BungeeDriverImpl()
                : new BungeeDriverEmpty();

        getCommand("tpa").setExecutor(new TpaExecutor());
        getCommand("tpaccept").setExecutor(new TpAcceptExecutor());
        getCommand("tpdeny").setExecutor(new TpDenyExecutor());
        getCommand("back").setExecutor(new BackExecutor());

        getServer().getPluginManager().registerEvents(new RequestInventoryClickEvent(), this);
        getServer().getPluginManager().registerEvents(new HeadCachePlayerJoinEvent(), this);
        getServer().getPluginManager().registerEvents(new BackPlayerDeathEvent(), this);
        getServer().getPluginManager().registerEvents(new BackPlayerQuitEvent(), this);

        getServer().getMessenger().registerIncomingPluginChannel(this, "BungeeCord", bungeeDriver);
        getServer().getMessenger().registerOutgoingPluginChannel(this, "BungeeCord");

        getBungeeDriver().registerListener(new PingMessageListener());
        getBungeeDriver().registerListener(new RequestMessageListener());
        getBungeeDriver().registerListener(new PlayerListMessageListener());
        getBungeeDriver().askForServerName();

        if (getHeadCacheService() != null) {
            getHeadCacheService().startCacheQueue();
            getHeadCacheService().startCacheRefresh();
        }

        super.onEnable();
    }

    @Override
    public void onDisable() {
        HandlerList.unregisterAll(this);

        getBungeeDriver().unregisterListeners();

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
