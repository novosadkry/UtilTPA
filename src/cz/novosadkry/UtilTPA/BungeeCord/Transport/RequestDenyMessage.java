package cz.novosadkry.UtilTPA.BungeeCord.Transport;

public class RequestDenyMessage extends Message {
    public String to;
    public String from;

    public String getTo() {
        return to;
    }

    public String getFrom() {
        return from;
    }

    @Override
    public void send(String player) {

    }
}
