package cz.novosadkry.UtilTPABungee.Transport.Drivers;

import cz.novosadkry.UtilBungee.Drivers.BungeeDriver;
import cz.novosadkry.UtilBungee.Transport.Messages.IMessage;
import cz.novosadkry.UtilBungee.Transport.Resolvers.IMessageResolverPool;
import cz.novosadkry.UtilTPABungee.Main;
import net.md_5.bungee.api.event.PluginMessageEvent;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.event.EventHandler;

public class PluginMessageHandler extends BungeeDriver implements Listener {

    public PluginMessageHandler(IMessageResolverPool resolverPool) {
        super(resolverPool);
    }

    @EventHandler
    public void on(PluginMessageEvent event) {
        if (!event.getTag().equalsIgnoreCase(Main.getChannel()))
            return;

        handleData(event.getData());
    }

    @Override
    public void sendMessage(IMessage msg) {

    }
}
