package cz.novosadkry.UtilTPA.BungeeCord.Transport.Listeners;

import cz.novosadkry.UtilTPA.BungeeCord.Transport.Abstract.Message;
import cz.novosadkry.UtilTPA.BungeeCord.Transport.Abstract.MessageListener;
import cz.novosadkry.UtilTPA.BungeeCord.Transport.Messages.PlayerListMessage;
import org.bukkit.Bukkit;

public class PlayerListMessageListener implements MessageListener {
    @Override
    public void onMessage(Message msg) {
        if (msg instanceof PlayerListMessage)
            onMessage((PlayerListMessage) msg);
    }

    public void onMessage(PlayerListMessage msg) {
        Bukkit.broadcastMessage(String.join(", ", msg.getPlayerList()));
    }
}
