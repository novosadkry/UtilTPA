package cz.novosadkry.UtilBungee.Transport.Messages;

import cz.novosadkry.UtilBungee.Transport.Handlers.IMessageHandler;

public interface IMessage {
    void send(IMessageHandler handler);

    byte[] toBytes();

    IMessage on(IMessageListener callback);
}
