package cz.novosadkry.UtilTPABungee.Transport.Drivers;

import cz.novosadkry.UtilBungee.Transport.Handlers.MessageHandler;
import cz.novosadkry.UtilBungee.Transport.Messages.IMessage;
import cz.novosadkry.UtilBungee.Transport.Resolvers.IMessageResolverPool;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.event.PluginMessageEvent;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.event.EventHandler;

public class PluginMessageHandler extends MessageHandler implements Listener {
    private final ProxyServer proxy;
    private final String channel;

    public PluginMessageHandler(ProxyServer proxy, String channel, IMessageResolverPool resolverPool) {
        super(resolverPool);
        this.proxy = proxy;
        this.channel = channel;
    }

    @EventHandler
    public void on(PluginMessageEvent event) {
        if (!event.getTag().equalsIgnoreCase(channel))
            return;

        handleData(event.getData());
    }

    @Override
    public void sendMessage(IMessage msg) {
        msg.send(this);
    }

    public ProxyServer getProxy() {
        return proxy;
    }

    public String getChannel() {
        return channel;
    }
}
