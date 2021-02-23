package cz.novosadkry.UtilTPA.BungeeCord.Transport;

import cz.novosadkry.UtilTPA.BungeeCord.Transport.Abstract.MessageType;

public class RequestDenyMessage extends RequestMessage {
    public RequestDenyMessage(String from, String to) {
        super(from, to);
    }

    @Override
    public MessageType getType() {
        return MessageType.REQUEST_DENY;
    }

    @Override
    public void send(String player) {

    }
}
