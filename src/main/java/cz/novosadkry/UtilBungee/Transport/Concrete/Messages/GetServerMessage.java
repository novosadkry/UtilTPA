package cz.novosadkry.UtilBungee.Transport.Concrete.Messages;

import com.google.common.io.ByteArrayDataOutput;
import com.google.common.io.ByteStreams;

public class GetServerMessage extends Message {
    protected String server;

    public String getServer() {
        return server;
    }

    public GetServerMessage setServer(String server) {
        this.server = server;
        return this;
    }

    @Override
    public byte[] toBytes() {
        ByteArrayDataOutput header = ByteStreams.newDataOutput();

        // Write GetServer header
        header.writeUTF("GetServer");

        return header.toByteArray();
    }

    @Override
    public String getName() {
        return "GET_SERVER";
    }
}
