package cz.novosadkry.UtilTPA.BungeeCord.Transport.Listeners;

import cz.novosadkry.UtilTPA.BungeeCord.Transport.Abstract.Message;
import cz.novosadkry.UtilTPA.BungeeCord.Transport.Abstract.MessageListener;
import cz.novosadkry.UtilTPA.BungeeCord.Transport.Messages.PingMessage;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

public class PingMessageListener implements MessageListener {
    @Override
    public void onMessage(Message msg) {
        onMessage((PingMessage) msg);
    }

    public void onMessage(PingMessage msg) {
        Player player = Bukkit.getPlayer(msg.getTo());

        if (player != null)
            player.sendMessage(String.format("%s: %s", msg.getFrom(), msg.getMessage()));
    }
}
