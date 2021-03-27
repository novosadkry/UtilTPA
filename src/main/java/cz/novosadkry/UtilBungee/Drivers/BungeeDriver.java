package cz.novosadkry.UtilBungee.Drivers;

import cz.novosadkry.UtilBungee.Transport.Messages.IMessage;
import cz.novosadkry.UtilBungee.Transport.Messages.MessageEventHandler;
import cz.novosadkry.UtilBungee.Transport.Resolvers.IMessageResolverPool;

public abstract class BungeeDriver extends MessageEventHandler implements IBungeeDriver {
    protected IMessageResolverPool resolverPool;

    protected BungeeDriver(IMessageResolverPool resolverPool) {
        this.resolverPool = resolverPool;
    }

    @Override
    public void handleData(byte[] data) {
        IMessage msg = resolverPool.resolve(data);

        if (msg != null)
            notifyListeners(msg);
    }
}
