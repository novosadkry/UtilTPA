package cz.novosadkry.UtilTPA.BungeeCord.Transport;

import com.google.common.io.ByteArrayDataInput;

public abstract class Message {
    public abstract void send(String player);

    public static Message resolve(ByteArrayDataInput data) {
        MessageType type = MessageType.values()[data.readShort()];

        switch (type) {
            case PING:
                return PingMessage.resolve(data);

            case REQUEST:
                return null;

            case REQUEST_ACCEPT:
                return null;

            case REQUEST_DENY:
                return null;

            default:
                return null;
        }
    }
}
