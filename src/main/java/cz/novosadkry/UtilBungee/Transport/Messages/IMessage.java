package cz.novosadkry.UtilBungee.Transport.Messages;

import cz.novosadkry.UtilBungee.Drivers.IBungeeDriver;

public interface IMessage {
    void send(IBungeeDriver driver);

    byte[] toBytes();

    IMessage on(IMessageListener callback);
}
