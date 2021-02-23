package cz.novosadkry.UtilTPA.BungeeCord.Transport.Abstract;

import com.google.common.io.ByteArrayDataInput;
import cz.novosadkry.UtilTPA.BungeeCord.Transport.PingMessage;
import cz.novosadkry.UtilTPA.BungeeCord.Transport.RequestAcceptMessage;
import cz.novosadkry.UtilTPA.BungeeCord.Transport.RequestDenyMessage;
import cz.novosadkry.UtilTPA.BungeeCord.Transport.RequestMessage;

public abstract class Message {
    public abstract MessageType getType();

    public abstract void send(String player);

    public static Message resolve(ByteArrayDataInput data) {
        MessageType type = MessageType.values()[data.readShort()];

        switch (type) {
            case PING:
                return PingMessage.resolve(data);

            case REQUEST:
                return RequestMessage.resolve(data);

            case REQUEST_ACCEPT:
                return RequestAcceptMessage.resolve(data);

            case REQUEST_DENY:
                return RequestDenyMessage.resolve(data);

            default:
                return null;
        }
    }
}
