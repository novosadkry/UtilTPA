package cz.novosadkry.UtilTPA.BungeeCord.Transport;

public class RequestDenyMessage extends RequestMessage {
    public RequestDenyMessage(String from, String to) {
        super(from, to);
    }

    @Override
    public void send(String player) {

    }
}
