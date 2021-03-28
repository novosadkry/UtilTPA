package cz.novosadkry.UtilTPABungee.Transport.Messages;

import cz.novosadkry.UtilBungee.Transport.Handlers.IMessageHandler;
import cz.novosadkry.UtilBungee.Transport.Messages.IMessage;
import cz.novosadkry.UtilBungee.Transport.Messages.IMessageListener;
import cz.novosadkry.UtilTPABungee.Main;
import cz.novosadkry.UtilTPABungee.Transport.Drivers.PluginMessageHandler;

public abstract class Message implements IMessage {
    public void send(IMessageHandler handler) {
        if (handler == null)
            return;

        handler.sendMessage(this);
    }

    public abstract byte[] toBytes();

    public Message on(IMessageListener callback) {
        PluginMessageHandler handler = Main.getMessageHandler();
        handler.registerListener(callback);
        return this;
    }
}
