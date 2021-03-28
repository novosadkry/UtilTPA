package cz.novosadkry.UtilTPABungee;

import cz.novosadkry.UtilBungee.Transport.Resolvers.MessageResolverPool;
import cz.novosadkry.UtilTPABungee.Transport.Drivers.PluginMessageHandler;
import cz.novosadkry.UtilTPABungee.Transport.Listeners.PingMessageListener;
import cz.novosadkry.UtilTPABungee.Transport.Resolvers.PingMessageResolver;
import net.md_5.bungee.api.plugin.Plugin;

public class Main extends Plugin {
    private static final String channel = "UtilTPA";

    private static Main instance;
    private static PluginMessageHandler messageHandler;

    @Override
    public void onEnable() {
        messageHandler = new PluginMessageHandler(
                getProxy(),
                getChannel(),
                new MessageResolverPool()
                        .registerResolver(new PingMessageResolver())
        );

        messageHandler.registerListener(new PingMessageListener());

        getProxy().registerChannel(channel);
        getProxy().getPluginManager().registerListener(this, messageHandler);

        super.onEnable();
    }

    public String getChannel() {
        return channel;
    }

    public static Main getInstance() {
        return instance;
    }

    public static PluginMessageHandler getMessageHandler() {
        return messageHandler;
    }
}
