package cz.novosadkry.UtilTPA.BungeeCord.Transport.Abstract;

public interface MessageListener {
    /** Should be removed from the invocation list if returns <code>false</code> */
    boolean onMessage(Message msg);
}
