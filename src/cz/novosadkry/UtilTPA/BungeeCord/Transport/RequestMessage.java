package cz.novosadkry.UtilTPA.BungeeCord.Transport;

import com.google.common.io.ByteArrayDataInput;
import com.google.common.io.ByteArrayDataOutput;
import com.google.common.io.ByteStreams;
import cz.novosadkry.UtilTPA.BungeeCord.Transport.Abstract.Message;
import cz.novosadkry.UtilTPA.BungeeCord.Transport.Abstract.MessageType;
import cz.novosadkry.UtilTPA.Main;
import cz.novosadkry.UtilTPA.Request.Request;
import org.bukkit.Bukkit;

public class RequestMessage extends Message {
    protected String from;
    protected String to;

    public RequestMessage(String from, String to) {
        this.from = from;
        this.to = to;
    }

    public RequestMessage(Request request) {
        this(request.getFrom(), request.getTo());
    }

    public String getFrom() {
        return from;
    }

    public String getTo() {
        return to;
    }

    @Override
    public MessageType getType() {
        return MessageType.REQUEST;
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
        body.writeShort(MessageType.REQUEST.ordinal());
        body.writeUTF(from);
        body.writeUTF(to);

        // Append message to header
        byte[] bodyBytes = body.toByteArray();
        header.writeShort(bodyBytes.length);
        header.write(bodyBytes);

        Bukkit.getServer().sendPluginMessage(Main.getInstance(), "BungeeCord", header.toByteArray());
    }

    public static Message resolve(ByteArrayDataInput data) {
        String from = data.readUTF();
        String to = data.readUTF();

        return new RequestMessage(from, to);
    }
}
