package cz.novosadkry.UtilTPABungee.Transport.Listeners;

import cz.novosadkry.UtilBungee.Transport.Concrete.Messages.PingMessage;
import cz.novosadkry.UtilBungee.Transport.Messages.IMessage;
import cz.novosadkry.UtilBungee.Transport.Messages.IMessageListener;
import cz.novosadkry.UtilTPABungee.Main;
import cz.novosadkry.UtilTPABungee.Transport.Messages.TargetMessage;
import net.md_5.bungee.api.connection.ProxiedPlayer;

public class PingMessageListener implements IMessageListener {
    @Override
    public void onMessage(IMessage msg) {
        if (msg instanceof PingMessage)
            onMessage((PingMessage) msg);
    }

    public void onMessage(PingMessage msg) {
        ProxiedPlayer player = Main.getInstance().getProxy().getPlayer(msg.getTo());

        if (player != null)
            Main.getMessageHandler().sendMessage(new TargetMessage(player, msg));
    }
}
