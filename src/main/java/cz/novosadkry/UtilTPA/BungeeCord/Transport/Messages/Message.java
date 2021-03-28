package cz.novosadkry.UtilTPA.BungeeCord.Transport.Messages;

import cz.novosadkry.UtilBungee.Transport.Handlers.IMessageHandler;
import cz.novosadkry.UtilBungee.Transport.Messages.IMessage;
import cz.novosadkry.UtilBungee.Transport.Messages.IMessageListener;
import cz.novosadkry.UtilTPA.BungeeCord.Drivers.BungeeDriver;
import cz.novosadkry.UtilTPA.Main;

public abstract class Message implements IMessage {
    public void send(IMessageHandler handler) {
        if (handler == null)
            return;

        handler.sendMessage(this);
    }

    public abstract byte[] toBytes();

    public Message on(IMessageListener callback) {
        BungeeDriver bungeeDriver = Main.getService(BungeeDriver.class);
        bungeeDriver.registerListener(callback);
        return this;
    }

    public abstract String getName();
}
