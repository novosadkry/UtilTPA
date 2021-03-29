package cz.novosadkry.UtilTPA.BungeeCord.Listeners;

import cz.novosadkry.UtilBungee.Transport.Concrete.Messages.PingMessage;
import cz.novosadkry.UtilBungee.Transport.Messages.IMessage;
import cz.novosadkry.UtilBungee.Transport.Messages.IMessageListener;
import org.bukkit.Bukkit;

public class PingMessageListener implements IMessageListener {
    @Override
    public void onMessage(IMessage msg) {
        if (msg instanceof PingMessage) {
            onMessage((PingMessage) msg);
        }
    }

    public void onMessage(PingMessage msg) {
        Bukkit.getPlayerExact(msg.getTo()).sendMessage(msg.getMessage());
    }
}
