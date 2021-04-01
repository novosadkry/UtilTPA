package cz.novosadkry.UtilBungee.Transport.Concrete.Messages;

import com.google.common.io.ByteArrayDataOutput;
import com.google.common.io.ByteStreams;

// Example
public class PingMessage extends Message {
    protected String to;
    protected String from;
    protected String message;

    public PingMessage(String from, String to, String message) {
        this.from = from;
        this.to = to;
        this.message = message;
    }

    public String getFrom() {
        return from;
    }

    public String getTo() {
        return to;
    }

    public String getMessage() {
        return message;
    }

    @Override
    public byte[] toBytes() {
        ByteArrayDataOutput data = ByteStreams.newDataOutput();

        // Write message data
        data.writeUTF("PING");
        data.writeUTF(from);
        data.writeUTF(to);
        data.writeUTF(message);

        return data.toByteArray();
    }

    @Override
    public String getName() {
        return "PING";
    }
}
