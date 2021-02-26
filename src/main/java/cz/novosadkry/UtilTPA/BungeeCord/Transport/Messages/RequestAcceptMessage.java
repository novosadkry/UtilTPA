package cz.novosadkry.UtilTPA.BungeeCord.Transport.Messages;

import cz.novosadkry.UtilTPA.BungeeCord.Transport.Abstract.MessageType;
import cz.novosadkry.UtilTPA.Request.Request;

public class RequestAcceptMessage extends RequestMessage {
    public RequestAcceptMessage(Request request) {
        super(request);
    }

    @Override
    public MessageType getType() {
        return MessageType.REQUEST_ACCEPT;
    }

    @Override
    public void send() {

    }
}
