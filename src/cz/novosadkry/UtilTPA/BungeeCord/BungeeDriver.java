package cz.novosadkry.UtilTPA.BungeeCord;

import com.google.common.io.ByteArrayDataInput;
import com.google.common.io.ByteStreams;
import cz.novosadkry.UtilTPA.BungeeCord.Transport.Abstract.Message;
import cz.novosadkry.UtilTPA.BungeeCord.Transport.PingMessage;
import cz.novosadkry.UtilTPA.BungeeCord.Transport.RequestMessage;
import cz.novosadkry.UtilTPA.Request.Request;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.messaging.PluginMessageListener;

public class BungeeDriver implements PluginMessageListener {

    @Override
    public void onPluginMessageReceived(String channel, Player player, byte[] bytes) {
        if (!channel.equalsIgnoreCase("BungeeCord"))
            return;

        ByteArrayDataInput data = ByteStreams.newDataInput(bytes);
        final String subChannel = data.readUTF();
        final short length = data.readShort();

        if (!subChannel.equalsIgnoreCase("UtilTPA"))
            return;

        Message msg = Message.resolve(data);

        if (msg instanceof PingMessage) {
            PingMessage pingMsg = (PingMessage) msg;
            player.sendMessage(String.format("%s: %s", pingMsg.getFrom(), pingMsg.getMessage()));
        }

        else if (msg instanceof RequestMessage) {
            RequestMessage requestMsg = (RequestMessage) msg;
            Player from = Bukkit.getPlayer(requestMsg.getFrom());
            Player to = Bukkit.getPlayer(requestMsg.getTo());

            Request request = new Request(from, to);

            switch (msg.getType()) {
                case REQUEST:
                    break;

                case REQUEST_ACCEPT:
                    break;

                case REQUEST_DENY:
                    break;
            }
        }
    }
}