package cz.novosadkry.UtilTPA.BungeeCord.Transport;

import cz.novosadkry.UtilTPA.BungeeCord.Transport.Abstract.MessageType;

public class RequestAcceptMessage extends RequestMessage {
    public RequestAcceptMessage(String from, String to) {
        super(from, to);
    }

    @Override
    public MessageType getType() {
        return MessageType.REQUEST_ACCEPT;
    }

    @Override
    public void send() {

    }
}
