package cz.novosadkry.UtilTPABungee.Transport.Messages;

import cz.novosadkry.UtilBungee.Transport.Handlers.IMessageHandler;
import cz.novosadkry.UtilBungee.Transport.Messages.IMessage;
import cz.novosadkry.UtilBungee.Transport.Messages.IMessageListener;

public abstract class Message implements IMessage {
    private IMessageListener callback;

    public abstract byte[] toBytes();

    public Message on(IMessageListener callback) {
        this.callback = callback;
        return this;
    }

    @Override
    public void send(IMessageHandler handler) {
        if (callback != null)
            handler.registerListener(callback);
    }
}
