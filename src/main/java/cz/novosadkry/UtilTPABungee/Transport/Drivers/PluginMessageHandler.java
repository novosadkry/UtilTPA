package cz.novosadkry.UtilTPABungee.Transport.Drivers;

import cz.novosadkry.UtilBungee.Transport.Handlers.MessageHandler;
import cz.novosadkry.UtilBungee.Transport.Messages.IMessage;
import cz.novosadkry.UtilBungee.Transport.Resolvers.IMessageResolverPool;
import cz.novosadkry.UtilTPABungee.Transport.Messages.BroadcastMessage;
import cz.novosadkry.UtilTPABungee.Transport.Messages.TargetMessage;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.connection.ProxiedPlayer;
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
        if (msg instanceof BroadcastMessage)
            broadcastMessage((BroadcastMessage) msg);

        else if (msg instanceof TargetMessage)
            sendMessage((TargetMessage) msg);

        else throw new UnsupportedOperationException();
    }

    public void sendMessage(ProxiedPlayer target, IMessage msg) {
        sendMessage(new TargetMessage(target, msg));
    }

    public void sendMessage(TargetMessage msg) {
        msg.getTarget().getServer().getInfo().sendData(getChannel(), msg.toBytes());
    }

    public void broadcastMessage(IMessage msg) {
        broadcastMessage(new BroadcastMessage(msg));
    }

    public void broadcastMessage(BroadcastMessage msg) {
        getProxy().getServers().forEach((k, v) -> v.sendData(getChannel(), msg.toBytes()));
    }

    public ProxyServer getProxy() {
        return proxy;
    }

    public String getChannel() {
        return channel;
    }
}
