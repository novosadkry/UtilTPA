package cz.novosadkry.UtilTPA.BungeeCord;

import com.google.common.io.ByteArrayDataInput;
import com.google.common.io.ByteStreams;
import cz.novosadkry.UtilTPA.BungeeCord.Transport.Abstract.Message;
import cz.novosadkry.UtilTPA.BungeeCord.Transport.PingMessage;
import cz.novosadkry.UtilTPA.BungeeCord.Transport.PlayerListMessage;
import cz.novosadkry.UtilTPA.BungeeCord.Transport.RequestMessage;
import cz.novosadkry.UtilTPA.Request.Request;
import cz.novosadkry.UtilTPA.Request.RequestManager;
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

        if (subChannel.equalsIgnoreCase("UtilTPA"))
        {
            final short length = data.readShort();
            Message msg = Message.resolve(data);

            if (msg instanceof PingMessage) {
                PingMessage pingMsg = (PingMessage) msg;
                player.sendMessage(String.format("%s: %s", pingMsg.getFrom(), pingMsg.getMessage()));
            }

            else if (msg instanceof RequestMessage) {
                RequestManager requestManager = RequestManager.getInstance();

                RequestMessage requestMsg = (RequestMessage) msg;
                Request request = new Request(requestMsg.getFrom(), requestMsg.getTo());

                switch (msg.getType()) {
                    case REQUEST:
                        requestManager.sendRequest(request);
                        break;

                    case REQUEST_ACCEPT:
                        break;

                    case REQUEST_DENY:
                        break;
                }
            }
        }

        else if (subChannel.equalsIgnoreCase("PlayerList")) {
            PlayerListMessage listMsg = (PlayerListMessage) PlayerListMessage.resolve(data);
            Bukkit.broadcastMessage(String.join(", ", listMsg.getPlayerList()));
        }
    }
}
