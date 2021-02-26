package cz.novosadkry.UtilTPA.BungeeCord.Transport.Abstract;

import com.google.common.io.ByteArrayDataInput;
import cz.novosadkry.UtilTPA.BungeeCord.Transport.Messages.*;

public abstract class Message {
    public abstract MessageType getType();

    public abstract void send();

    public static Message resolve(ByteArrayDataInput data) {
        final String subChannel = data.readUTF();

        if (subChannel.equalsIgnoreCase("UtilTPA")) {
            final short length = data.readShort();
            MessageType type = MessageType.values()[data.readShort()];

            switch (type) {
                case PING: return PingMessage.resolve(data);
                case REQUEST: return RequestMessage.resolve(data);
                case REQUEST_ACCEPT: return RequestAcceptMessage.resolve(data);
                case REQUEST_DENY: return RequestDenyMessage.resolve(data);
                default: return null;
            }
        }

        else if (subChannel.equalsIgnoreCase("PlayerList")) {
            return PlayerListMessage.resolve(data);
        }

        return null;
    }
}
