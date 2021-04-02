package cz.novosadkry.UtilBungee.Transport.Concrete.Messages;

import com.google.common.io.ByteArrayDataOutput;
import com.google.common.io.ByteStreams;
import cz.novosadkry.UtilTPA.Requests.Request;

public class RequestDenyMessage extends RequestMessage {
    protected String reason;

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
    public byte[] toBytes() {
        ByteArrayDataOutput header = ByteStreams.newDataOutput();

        // Write ForwardToPlayer header
        header.writeUTF("ForwardToPlayer");
        header.writeUTF(request.getFrom().getName());
        header.writeUTF("UtilTPA");

        ByteArrayDataOutput body = ByteStreams.newDataOutput();

        // Write message data
        body.writeUTF("REQUEST_DENY");
        body.writeUTF(request.getFrom().getName());
        body.writeUTF(request.getTo().getName());
        body.writeUTF(reason);

        // Append message to header
        byte[] bodyBytes = body.toByteArray();
        header.writeShort(bodyBytes.length);
        header.write(bodyBytes);

        return header.toByteArray();
    }

    @Override
    public String getName() {
        return "REQUEST_DENY";
    }
}
