package cz.novosadkry.UtilTPA.BungeeCord.Transport.Messages;

import com.google.common.io.ByteArrayDataInput;
import com.google.common.io.ByteArrayDataOutput;
import com.google.common.io.ByteStreams;
import cz.novosadkry.UtilTPA.BungeeCord.Transport.Abstract.Message;
import cz.novosadkry.UtilTPA.BungeeCord.Transport.Abstract.MessageType;
import cz.novosadkry.UtilTPA.Main;
import org.bukkit.entity.Player;

public class ConnectMessage extends Message {
    protected Player player;
    protected String server;

    public ConnectMessage(Player player, String server) {
        this.player = player;
        this.server = server;
    }

    public String getServer() {
        return server;
    }

    @Override
    public MessageType getType() {
        return MessageType.CONNECT;
    }

    @Override
    public void send() {
        ByteArrayDataOutput header = ByteStreams.newDataOutput();

        // Write Connect header
        header.writeUTF("Connect");
        header.writeUTF(server);

        player.sendPluginMessage(Main.getInstance(), "BungeeCord", header.toByteArray());
    }

    public static Message resolve(ByteArrayDataInput data) {
        return null;
    }
}
