package cz.novosadkry.UtilBungee.Transport.Concrete.Messages;

import com.google.common.io.ByteArrayDataOutput;
import com.google.common.io.ByteStreams;
import cz.novosadkry.UtilTPA.Requests.Request;

public class RequestMessage extends Message {
    protected Request request;

    public RequestMessage(Request request) {
        this.request = request;
    }

    public Request getRequest() {
        return request;
    }

    @Override
    public byte[] toBytes() {
        ByteArrayDataOutput header = ByteStreams.newDataOutput();

        // Write ForwardToPlayer header
        header.writeUTF("ForwardToPlayer");
        header.writeUTF(request.getTo().getName());
        header.writeUTF("UtilTPA");

        ByteArrayDataOutput body = ByteStreams.newDataOutput();

        // Write message data
        body.writeUTF("REQUEST");
        body.writeUTF(request.getFrom().getName());
        body.writeUTF(request.getTo().getName());

        // Append message to header
        byte[] bodyBytes = body.toByteArray();
        header.writeShort(bodyBytes.length);
        header.write(bodyBytes);

        return header.toByteArray();
    }

    @Override
    public String getName() {
        return "REQUEST";
    }
}
