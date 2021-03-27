package cz.novosadkry.UtilTPABungee.Transport.Messages;

import cz.novosadkry.UtilBungee.Drivers.IBungeeDriver;
import cz.novosadkry.UtilBungee.Transport.Messages.IMessage;
import cz.novosadkry.UtilBungee.Transport.Messages.IMessageListener;
import cz.novosadkry.UtilTPABungee.Main;
import cz.novosadkry.UtilTPABungee.Transport.Drivers.PluginMessageHandler;

public abstract class Message implements IMessage {
    public void send(IBungeeDriver driver) {
        if (driver == null)
            return;

        driver.sendMessage(this);
    }

    public abstract byte[] toBytes();

    public Message on(IMessageListener callback) {
        PluginMessageHandler handler = Main.getMessageHandler();
        handler.registerListener(callback);
        return this;
    }
}
