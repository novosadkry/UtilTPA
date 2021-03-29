package cz.novosadkry.UtilTPABungee.Transport.Listeners;

import cz.novosadkry.UtilBungee.Transport.Concrete.Messages.PingMessage;
import cz.novosadkry.UtilBungee.Transport.Messages.IMessage;
import cz.novosadkry.UtilBungee.Transport.Messages.IMessageListener;
import cz.novosadkry.UtilTPA.Log;
import cz.novosadkry.UtilTPABungee.Main;

public class PingMessageListener implements IMessageListener {
    @Override
    public void onMessage(IMessage msg) {
        if (msg instanceof PingMessage)
            onMessage((PingMessage) msg);
    }

    public void onMessage(PingMessage msg) {
        Log.info(msg.getMessage());
        Main.getMessageHandler().sendMessage(msg);
    }
}
