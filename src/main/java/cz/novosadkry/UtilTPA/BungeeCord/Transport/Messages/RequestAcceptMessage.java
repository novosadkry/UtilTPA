package cz.novosadkry.UtilTPA.BungeeCord.Transport.Messages;

import com.google.common.io.ByteArrayDataInput;
import com.google.common.io.ByteArrayDataOutput;
import com.google.common.io.ByteStreams;
import cz.novosadkry.UtilTPA.BungeeCord.Transport.Abstract.Message;
import cz.novosadkry.UtilTPA.BungeeCord.Transport.Abstract.MessageType;
import cz.novosadkry.UtilTPA.Main;
import cz.novosadkry.UtilTPA.Request.Request;
import org.bukkit.Bukkit;

public class RequestAcceptMessage extends RequestMessage {
    protected String server;

    public RequestAcceptMessage(Request request) {
        super(request);
    }

    public String getServer() {
        return server;
    }

    public RequestAcceptMessage setServer(String server) {
        this.server = server;
        return this;
    }

    @Override
    public MessageType getType() {
        return MessageType.REQUEST_ACCEPT;
    }

    @Override
    public void send() {
        ByteArrayDataOutput header = ByteStreams.newDataOutput();

        // Write ForwardToPlayer header
        header.writeUTF("ForwardToPlayer");
        header.writeUTF(request.getFrom().getName());
        header.writeUTF("UtilTPA");

        ByteArrayDataOutput body = ByteStreams.newDataOutput();

        // Write message data
        body.writeShort(getType().ordinal());
        body.writeUTF(request.getFrom().getName());
        body.writeUTF(request.getTo().getName());
        body.writeUTF(server);

        // Append message to header
        byte[] bodyBytes = body.toByteArray();
        header.writeShort(bodyBytes.length);
        header.write(bodyBytes);

        Bukkit.getServer().sendPluginMessage(Main.getInstance(), "BungeeCord", header.toByteArray());
    }

    public static Message resolve(ByteArrayDataInput data) {
        String from = data.readUTF();
        String to = data.readUTF();
        String server = data.readUTF();

        return new RequestAcceptMessage(new Request(from, to))
                .setServer(server);
    }
}
