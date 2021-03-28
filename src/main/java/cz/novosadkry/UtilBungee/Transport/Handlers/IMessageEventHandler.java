package cz.novosadkry.UtilBungee.Transport.Handlers;

import cz.novosadkry.UtilBungee.Transport.Messages.IMessageListener;

public interface IMessageEventHandler {
    IMessageEventHandler registerListener(IMessageListener listener);

    IMessageEventHandler unregisterListener(IMessageListener listener);

    IMessageEventHandler unregisterListeners();
}
