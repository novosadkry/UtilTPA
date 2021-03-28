package cz.novosadkry.UtilBungee.Transport.Handlers;

import cz.novosadkry.UtilBungee.Transport.Messages.IMessage;
import cz.novosadkry.UtilBungee.Transport.Resolvers.IMessageResolverPool;

public abstract class MessageHandler extends MessageEventHandler implements IMessageHandler {
    protected IMessageResolverPool resolverPool;

    protected MessageHandler(IMessageResolverPool resolverPool) {
        this.resolverPool = resolverPool;
    }

    @Override
    public void handleData(byte[] data) {
        IMessage msg = resolverPool.resolve(data);

        if (msg != null)
            notifyListeners(msg);
    }
}
