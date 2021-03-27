package cz.novosadkry.UtilTPA.BungeeCord.Transport.Messages;

import com.google.common.io.ByteArrayDataOutput;
import com.google.common.io.ByteStreams;
import cz.novosadkry.UtilTPA.Request.Request;

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
    public byte[] toBytes() {
        ByteArrayDataOutput header = ByteStreams.newDataOutput();

        // Write ForwardToPlayer header
        header.writeUTF("ForwardToPlayer");
        header.writeUTF(request.getFrom().getName());
        header.writeUTF("UtilTPA");

        ByteArrayDataOutput body = ByteStreams.newDataOutput();

        // Write message data
        body.writeUTF("REQUEST_ACCEPT");
        body.writeUTF(request.getFrom().getName());
        body.writeUTF(request.getTo().getName());
        body.writeUTF(server);

        // Append message to header
        byte[] bodyBytes = body.toByteArray();
        header.writeShort(bodyBytes.length);
        header.write(bodyBytes);

        return header.toByteArray();
    }

    @Override
    public String getName() {
        return "REQUEST_ACCEPT";
    }
}
