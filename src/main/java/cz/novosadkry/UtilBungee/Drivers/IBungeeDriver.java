package cz.novosadkry.UtilBungee.Drivers;

import cz.novosadkry.UtilBungee.Transport.Messages.IMessage;
import cz.novosadkry.UtilBungee.Transport.Messages.IMessageEventHandler;

public interface IBungeeDriver extends IMessageEventHandler {
    void sendMessage(IMessage msg);

    void handleData(byte[] data);
}
