package cz.novosadkry.UtilTPABungee.Transport.Messages;

import com.google.common.io.ByteArrayDataOutput;
import com.google.common.io.ByteStreams;
import net.md_5.bungee.api.connection.ProxiedPlayer;

public class PingMessage extends TargetMessage {
    private ProxiedPlayer from;
    private String message;

    public PingMessage(ProxiedPlayer from, ProxiedPlayer to, String message) {
        super(to);
        this.from = from;
        this.message = message;
    }

    public ProxiedPlayer getTo() {
        return getFrom();
    }

    public void setTo(ProxiedPlayer to) {
        setTarget(from);
    }

    public ProxiedPlayer getFrom() {
        return from;
    }

    public void setFrom(ProxiedPlayer from) {
        this.from = from;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    @Override
    @SuppressWarnings("UnstableApiUsage")
    public byte[] toBytes() {
        ByteArrayDataOutput data = ByteStreams.newDataOutput();

        // Write message data
        data.writeUTF("Ping");
        data.writeUTF(getFrom().getName());
        data.writeUTF(getTo().getName());
        data.writeUTF(getMessage());

        return data.toByteArray();
    }
}
