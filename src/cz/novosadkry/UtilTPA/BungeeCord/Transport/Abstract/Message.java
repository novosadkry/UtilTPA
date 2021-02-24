package cz.novosadkry.UtilTPA.BungeeCord.Transport.Abstract;

import com.google.common.io.ByteArrayDataInput;
import cz.novosadkry.UtilTPA.BungeeCord.Transport.Messages.PingMessage;
import cz.novosadkry.UtilTPA.BungeeCord.Transport.Messages.RequestAcceptMessage;
import cz.novosadkry.UtilTPA.BungeeCord.Transport.Messages.RequestDenyMessage;
import cz.novosadkry.UtilTPA.BungeeCord.Transport.Messages.RequestMessage;

public abstract class Message {
    public abstract MessageType getType();

    public abstract void send();

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
