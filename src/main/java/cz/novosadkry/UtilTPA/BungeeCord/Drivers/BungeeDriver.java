package cz.novosadkry.UtilTPA.BungeeCord.Drivers;

import cz.novosadkry.UtilBungee.Transport.Handlers.MessageHandler;
import cz.novosadkry.UtilBungee.Transport.Resolvers.IMessageResolverPool;
import cz.novosadkry.UtilTPA.Services.IService;
import org.bukkit.plugin.messaging.PluginMessageListener;

import java.util.List;

public abstract class BungeeDriver extends MessageHandler implements PluginMessageListener, IService {
    private final String channel;

    protected BungeeDriver(String channel, IMessageResolverPool resolverPool) {
        super(resolverPool);
        this.channel = channel;
    }

    public String getChannel() {
        return channel;
    }

    public abstract String getServerName();

    public abstract List<String> getPlayerList();
}
