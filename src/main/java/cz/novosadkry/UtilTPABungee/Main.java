package cz.novosadkry.UtilTPABungee;

import cz.novosadkry.UtilBungee.Transport.Resolvers.MessageResolverPool;
import cz.novosadkry.UtilTPABungee.Transport.Drivers.PluginMessageHandler;
import net.md_5.bungee.api.plugin.Plugin;

public class Main extends Plugin {
    private static final String channel = "UtilTPA";
    private static Main instance;

    private static PluginMessageHandler messageHandler;

    @Override
    public void onEnable() {
        messageHandler = new PluginMessageHandler(new MessageResolverPool()
                .registerResolver(null)
        );

        getProxy().registerChannel(channel);
        getProxy().getPluginManager().registerListener(this, messageHandler);

        super.onEnable();
    }

    public static PluginMessageHandler getMessageHandler() {
        return messageHandler;
    }

    public static String getChannel() {
        return channel;
    }

    public static Main getInstance() {
        return instance;
    }
}
