package cz.novosadkry.UtilTPA.BungeeCord.Transport.Messages;

import com.google.common.io.ByteArrayDataInput;
import com.google.common.io.ByteArrayDataOutput;
import com.google.common.io.ByteStreams;
import cz.novosadkry.UtilTPA.BungeeCord.Transport.Abstract.Message;
import cz.novosadkry.UtilTPA.BungeeCord.Transport.Abstract.MessageType;
import cz.novosadkry.UtilTPA.Main;
import org.bukkit.Bukkit;

public class GetServerMessage extends Message {
    protected String server;

    public String getServer() {
        return server;
    }

    private GetServerMessage setServer(String server) {
        this.server = server;
        return this;
    }

    @Override
    public MessageType getType() {
        return MessageType.GET_SERVER;
    }

    @Override
    public void send() {
        ByteArrayDataOutput header = ByteStreams.newDataOutput();

        // Write GetServer header
        header.writeUTF("GetServer");

        Bukkit.getServer().sendPluginMessage(Main.getInstance(), "BungeeCord", header.toByteArray());
    }

    public static Message resolve(ByteArrayDataInput data) {
        String server = data.readUTF();

        return new GetServerMessage().setServer(server);
    }
}
