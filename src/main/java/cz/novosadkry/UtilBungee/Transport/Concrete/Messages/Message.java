package cz.novosadkry.UtilBungee.Transport.Concrete.Messages;

import cz.novosadkry.UtilBungee.Transport.Handlers.IMessageHandler;
import cz.novosadkry.UtilBungee.Transport.Messages.IMessage;
import cz.novosadkry.UtilBungee.Transport.Messages.IMessageListener;

public abstract class Message implements IMessage {
    private IMessageListener callback;

    public void send(IMessageHandler handler) {
        if (handler == null)
            return;

        if (callback != null)
            handler.registerListener(callback);

        handler.sendMessage(this);
    }

    public abstract byte[] toBytes();

    public Message on(IMessageListener callback) {
        this.callback = callback;
        return this;
    }

    public abstract String getName();
}
