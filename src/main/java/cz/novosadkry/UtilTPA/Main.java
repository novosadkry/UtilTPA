package cz.novosadkry.UtilTPA;

import cz.novosadkry.UtilTPA.BungeeCord.Drivers.BungeeDriver;
import cz.novosadkry.UtilBungee.Transport.Resolvers.MessageResolverPool;
import cz.novosadkry.UtilTPA.BungeeCord.Drivers.BungeeDriverOffline;
import cz.novosadkry.UtilTPA.BungeeCord.Drivers.BungeeDriverOnline;
import cz.novosadkry.UtilBungee.Transport.Concrete.Resolvers.*;
import cz.novosadkry.UtilTPA.Commands.TPA.TabCompleters.TpAcceptTabCompleter;
import cz.novosadkry.UtilTPA.Commands.TPA.TabCompleters.TpDenyTabCompleter;
import cz.novosadkry.UtilTPA.Commands.TPA.TabCompleters.TpaTabCompleter;
import cz.novosadkry.UtilTPA.Heads.IHeadProvider;
import cz.novosadkry.UtilTPA.BungeeCord.Listeners.RequestMessageListener;
import cz.novosadkry.UtilTPA.Commands.Back.BackExecutor;
import cz.novosadkry.UtilTPA.Commands.Back.BackPlayerDeathListener;
import cz.novosadkry.UtilTPA.Commands.Back.BackPlayerQuitListener;
import cz.novosadkry.UtilTPA.Commands.TPA.TpAcceptExecutor;
import cz.novosadkry.UtilTPA.Commands.TPA.TpDenyExecutor;
import cz.novosadkry.UtilTPA.Commands.TPA.TpaExecutor;
import cz.novosadkry.UtilTPA.Heads.Listeners.HeadPlayerJoinListener;
import cz.novosadkry.UtilTPA.Heads.HeadProviderEmpty;
import cz.novosadkry.UtilTPA.Heads.Service.HeadCacheService;
import cz.novosadkry.UtilTPA.Request.Listeners.RequestPlayerSpawnListener;
import cz.novosadkry.UtilTPA.Localization.Locale;
import cz.novosadkry.UtilTPA.Services.IService;
import cz.novosadkry.UtilTPA.Services.IServiceProvider;
import cz.novosadkry.UtilTPA.Services.ServiceProvider;
import cz.novosadkry.UtilTPA.UI.RequestInventoryClickListener;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.event.HandlerList;
import org.bukkit.plugin.java.JavaPlugin;

public class Main extends JavaPlugin {
    private static Main instance;
    private static IServiceProvider serviceProvider;

    @Override
    public void onLoad() {
        instance = this;
        serviceProvider = new ServiceProvider();

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

        BungeeDriver bungeeDriver = config.getBoolean("bungeecord.enabled")
                ? new BungeeDriverOnline(
                    config.getLong("bungeecord.playerlist-tick"),
                    messageResolvers)
                : new BungeeDriverOffline();

        IHeadProvider headProvider =
                config.getBoolean("head-inventory.enabled") &&
                config.getBoolean("head-inventory.online-mode")
                    ? new HeadCacheService(
                        config.getLong("head-inventory.cache.expire-tick"),
                        config.getLong("head-inventory.cache.refresh-tick"),
                        config.getLong("head-inventory.cache.queue-tick"))
                    : new HeadProviderEmpty();

        getCommand("tpa").setExecutor(new TpaExecutor());
        getCommand("tpaccept").setExecutor(new TpAcceptExecutor());
        getCommand("tpdeny").setExecutor(new TpDenyExecutor());
        getCommand("back").setExecutor(new BackExecutor());

        getCommand("tpa").setTabCompleter(new TpaTabCompleter());
        getCommand("tpdeny").setTabCompleter(new TpDenyTabCompleter());
        getCommand("tpaccept").setTabCompleter(new TpAcceptTabCompleter());

        getServer().getPluginManager().registerEvents(new RequestPlayerSpawnListener(), this);
        getServer().getPluginManager().registerEvents(new RequestInventoryClickListener(), this);
        getServer().getPluginManager().registerEvents(new HeadPlayerJoinListener(), this);
        getServer().getPluginManager().registerEvents(new BackPlayerDeathListener(), this);
        getServer().getPluginManager().registerEvents(new BackPlayerQuitListener(), this);

        bungeeDriver.registerListener(new RequestMessageListener());

        getServiceProvider().add(bungeeDriver, true);
        getServiceProvider().add(headProvider, true);

        super.onEnable();
    }

    @Override
    public void onDisable() {
        HandlerList.unregisterAll(this);
        getServiceProvider().removeAll(true);

        super.onDisable();
    }

    public static Main getInstance() {
        return instance;
    }

    public static IServiceProvider getServiceProvider() {
        return serviceProvider;
    }

    public static <T extends IService> T getService(Class<T> clazz) {
        return Main.getServiceProvider().get(clazz);
    }
}
