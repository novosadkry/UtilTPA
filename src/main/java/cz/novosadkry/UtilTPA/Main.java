package cz.novosadkry.UtilTPA;

import cz.novosadkry.UtilTPA.BungeeCord.Drivers.BungeeDriver;
import cz.novosadkry.UtilTPA.BungeeCord.Drivers.Concrete.BungeeDriverOffline;
import cz.novosadkry.UtilTPA.BungeeCord.Drivers.Concrete.BungeeDriverOnline;
import cz.novosadkry.UtilTPA.BungeeCord.Transport.Resolvers.Concrete.*;
import cz.novosadkry.UtilTPA.BungeeCord.Transport.Resolvers.MessageResolverPool;
import cz.novosadkry.UtilTPA.Commands.TPA.TabCompleters.TpAcceptTabCompleter;
import cz.novosadkry.UtilTPA.Commands.TPA.TabCompleters.TpDenyTabCompleter;
import cz.novosadkry.UtilTPA.Commands.TPA.TabCompleters.TpaTabCompleter;
import cz.novosadkry.UtilTPA.Request.Listeners.RequestMessageListener;
import cz.novosadkry.UtilTPA.Commands.Back.BackExecutor;
import cz.novosadkry.UtilTPA.Commands.Back.BackPlayerDeathListener;
import cz.novosadkry.UtilTPA.Commands.Back.BackPlayerQuitListener;
import cz.novosadkry.UtilTPA.Commands.TPA.TpAcceptExecutor;
import cz.novosadkry.UtilTPA.Commands.TPA.TpDenyExecutor;
import cz.novosadkry.UtilTPA.Commands.TPA.TpaExecutor;
import cz.novosadkry.UtilTPA.Heads.Cache.HeadCachePlayerJoinListener;
import cz.novosadkry.UtilTPA.Heads.Service.HeadCacheService;
import cz.novosadkry.UtilTPA.Heads.Service.HeadCacheServiceOffline;
import cz.novosadkry.UtilTPA.Heads.Service.HeadCacheServiceOnline;
import cz.novosadkry.UtilTPA.Request.Listeners.RequestPlayerSpawnListener;
import cz.novosadkry.UtilTPA.Localization.Locale;
import cz.novosadkry.UtilTPA.UI.RequestInventoryClickListener;
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

        Locale.getInstance().load();

        MessageResolverPool messageResolvers = new MessageResolverPool()
                .registerResolver(new GetServerMessageResolver())
                .registerResolver(new PingMessageResolver())
                .registerResolver(new PlayerListMessageResolver())
                .registerResolver(new RequestAcceptMessageResolver())
                .registerResolver(new RequestDenyMessageResolver())
                .registerResolver(new RequestMessageResolver());

        bungeeDriver = config.getBoolean("bungeecord.enabled")
                ? new BungeeDriverOnline(
                    config.getLong("bungeecord.playerlist-tick"),
                    messageResolvers
                )
                : new BungeeDriverOffline();

        headCacheService =
                config.getBoolean("head-inventory.enabled") &&
                config.getBoolean("head-inventory.online-mode")
                    ? new HeadCacheServiceOnline(
                        config.getLong("head-inventory.cache.expire-tick"),
                        config.getLong("head-inventory.cache.refresh-tick"),
                        config.getLong("head-inventory.cache.queue-tick"))
                    : new HeadCacheServiceOffline();

        getCommand("tpa").setExecutor(new TpaExecutor());
        getCommand("tpaccept").setExecutor(new TpAcceptExecutor());
        getCommand("tpdeny").setExecutor(new TpDenyExecutor());
        getCommand("back").setExecutor(new BackExecutor());

        getCommand("tpa").setTabCompleter(new TpaTabCompleter());
        getCommand("tpdeny").setTabCompleter(new TpDenyTabCompleter());
        getCommand("tpaccept").setTabCompleter(new TpAcceptTabCompleter());

        getServer().getPluginManager().registerEvents(new RequestPlayerSpawnListener(), this);
        getServer().getPluginManager().registerEvents(new RequestInventoryClickListener(), this);
        getServer().getPluginManager().registerEvents(new HeadCachePlayerJoinListener(), this);
        getServer().getPluginManager().registerEvents(new BackPlayerDeathListener(), this);
        getServer().getPluginManager().registerEvents(new BackPlayerQuitListener(), this);

        getBungeeDriver().initialize();
        getBungeeDriver().registerListener(new RequestMessageListener());

        getHeadCacheService().initialize();

        super.onEnable();
    }

    @Override
    public void onDisable() {
        HandlerList.unregisterAll(this);

        getBungeeDriver().terminate();
        getHeadCacheService().terminate();

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
