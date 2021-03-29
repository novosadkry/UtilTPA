package cz.novosadkry.UtilTPABungee.Transport.Messages;

import cz.novosadkry.UtilBungee.Transport.Handlers.IMessageHandler;
import cz.novosadkry.UtilBungee.Transport.Messages.IMessage;
import cz.novosadkry.UtilBungee.Transport.Messages.IMessageListener;

public class BroadcastMessage implements IMessage {
    private final IMessage message;

    public BroadcastMessage(IMessage message) {
        this.message = message;
    }

    @Override
    public void send(IMessageHandler handler) {
        message.send(handler);
    }

    @Override
    public byte[] toBytes() {
        return message.toBytes();
    }

    @Override
    public IMessage on(IMessageListener callback) {
        return message.on(callback);
    }
}
