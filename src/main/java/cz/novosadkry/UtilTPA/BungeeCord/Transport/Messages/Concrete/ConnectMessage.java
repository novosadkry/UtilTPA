package cz.novosadkry.UtilTPA.BungeeCord.Transport.Messages.Concrete;

import com.google.common.io.ByteArrayDataOutput;
import com.google.common.io.ByteStreams;
import cz.novosadkry.UtilTPA.BungeeCord.Transport.Messages.Message;
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
    public byte[] toBytes() {
        ByteArrayDataOutput header = ByteStreams.newDataOutput();

        // Write Connect header
        header.writeUTF("Connect");
        header.writeUTF(server);

        return header.toByteArray();
    }

    @Override
    public String getName() {
        return "CONNECT";
    }
}
