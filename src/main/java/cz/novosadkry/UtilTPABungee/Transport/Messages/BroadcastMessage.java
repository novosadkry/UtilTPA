package cz.novosadkry.UtilTPABungee.Transport.Messages;

import cz.novosadkry.UtilBungee.Transport.Handlers.IMessageHandler;
import cz.novosadkry.UtilTPABungee.Transport.Drivers.PluginMessageHandler;

public abstract class BroadcastMessage extends Message {
    @Override
    public void send(IMessageHandler handler) {
        if (!(handler instanceof PluginMessageHandler))
            throw new UnsupportedOperationException();

        super.send(handler);

        PluginMessageHandler pHandler = (PluginMessageHandler) handler;
        pHandler.getProxy().getServers().forEach((k, v) -> v.sendData(pHandler.getChannel(), toBytes()));
    }
}
