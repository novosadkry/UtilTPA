package cz.novosadkry.UtilTPA.BungeeCord.Transport;

import com.google.common.io.ByteArrayDataInput;
import com.google.common.io.ByteArrayDataOutput;
import com.google.common.io.ByteStreams;
import cz.novosadkry.UtilTPA.BungeeCord.Transport.Abstract.Message;
import cz.novosadkry.UtilTPA.BungeeCord.Transport.Abstract.MessageType;
import cz.novosadkry.UtilTPA.Main;
import cz.novosadkry.UtilTPA.Request.Request;
import org.bukkit.Bukkit;

public class RequestDenyMessage extends RequestMessage {
    protected String reason;

    public RequestDenyMessage(String from, String to) {
        super(from, to);
    }

    public RequestDenyMessage(Request request) {
        super(request);
    }

    public String getReason() {
        return reason;
    }

    public RequestDenyMessage setReason(String reason) {
        this.reason = reason;
        return this;
    }

    @Override
    public MessageType getType() {
        return MessageType.REQUEST_DENY;
    }

    @Override
    public void send() {
        ByteArrayDataOutput header = ByteStreams.newDataOutput();

        // Write ForwardToPlayer header
        header.writeUTF("ForwardToPlayer");
        header.writeUTF(to);
        header.writeUTF("UtilTPA");

        ByteArrayDataOutput body = ByteStreams.newDataOutput();

        // Write message data
        body.writeShort(getType().ordinal());
        body.writeUTF(from);
        body.writeUTF(to);
        body.writeUTF(reason);

        // Append message to header
        byte[] bodyBytes = body.toByteArray();
        header.writeShort(bodyBytes.length);
        header.write(bodyBytes);

        Bukkit.getServer().sendPluginMessage(Main.getInstance(), "BungeeCord", header.toByteArray());
    }

    public static Message resolve(ByteArrayDataInput data) {
        String from = data.readUTF();
        String to = data.readUTF();
        String reason = data.readUTF();

        return new RequestDenyMessage(from, to).setReason(reason);
    }
}
