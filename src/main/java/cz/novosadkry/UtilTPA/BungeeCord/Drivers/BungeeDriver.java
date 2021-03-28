package cz.novosadkry.UtilTPA.BungeeCord.Drivers;

import cz.novosadkry.UtilBungee.Transport.Handlers.MessageHandler;
import cz.novosadkry.UtilBungee.Transport.Resolvers.IMessageResolverPool;
import cz.novosadkry.UtilTPA.Services.IService;
import org.bukkit.plugin.messaging.PluginMessageListener;

import java.util.List;

public abstract class BungeeDriver extends MessageHandler implements PluginMessageListener, IService {

    protected BungeeDriver(IMessageResolverPool resolverPool) {
        super(resolverPool);
    }

    public abstract String getServerName();

    public abstract List<String> getPlayerList();
}
