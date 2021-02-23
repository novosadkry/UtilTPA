package cz.novosadkry.UtilTPA.BungeeCord.Transport;

import com.google.common.io.ByteArrayDataInput;
import com.google.common.io.ByteArrayDataOutput;
import com.google.common.io.ByteStreams;
import cz.novosadkry.UtilTPA.BungeeCord.Transport.Abstract.Message;
import cz.novosadkry.UtilTPA.BungeeCord.Transport.Abstract.MessageType;
import cz.novosadkry.UtilTPA.Main;
import org.bukkit.Bukkit;

// Example
public class PingMessage extends Message {
    protected String from;
    protected String message;

    public PingMessage(String from, String message) {
        this.from = from;
        this.message = message;
    }

    public String getFrom() {
        return from;
    }

    public String getMessage() {
        return message;
    }

    @Override
    public void send(String player) {
        ByteArrayDataOutput header = ByteStreams.newDataOutput();

        // Write ForwardToPlayer header
        header.writeUTF("ForwardToPlayer");
        header.writeUTF(player);
        header.writeUTF("UtilTPA");

        ByteArrayDataOutput body = ByteStreams.newDataOutput();

        // Write message data
        body.writeShort(MessageType.PING.ordinal());
        body.writeUTF(from);
        body.writeUTF(message);

        // Append message to header
        byte[] bodyBytes = body.toByteArray();
        header.writeShort(bodyBytes.length);
        header.write(bodyBytes);

        Bukkit.getServer().sendPluginMessage(Main.getInstance(), "BungeeCord", header.toByteArray());
    }

    public static Message resolve(ByteArrayDataInput data) {
        String from = data.readUTF();
        String message = data.readUTF();

        return new PingMessage(from, message);
    }
}
