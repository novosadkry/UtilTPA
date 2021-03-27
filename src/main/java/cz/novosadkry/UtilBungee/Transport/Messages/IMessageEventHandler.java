package cz.novosadkry.UtilBungee.Transport.Messages;

public interface IMessageEventHandler {
    IMessageEventHandler registerListener(IMessageListener listener);

    IMessageEventHandler unregisterListener(IMessageListener listener);

    IMessageEventHandler unregisterListeners();
}
