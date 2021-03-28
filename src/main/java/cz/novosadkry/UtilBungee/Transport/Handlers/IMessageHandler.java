package cz.novosadkry.UtilBungee.Transport.Handlers;

import cz.novosadkry.UtilBungee.Transport.Messages.IMessage;

public interface IMessageHandler extends IMessageEventHandler {
    void sendMessage(IMessage msg);

    void handleData(byte[] data);
}
