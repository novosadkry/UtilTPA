package cz.novosadkry.UtilTPA.BungeeCord.Transport.Resolvers;

import cz.novosadkry.UtilTPA.BungeeCord.Transport.Messages.Message;

public class ResolveResult {
    private final boolean success;
    private final Message message;
    private final Exception exception;

    public ResolveResult(boolean success) {
        this(success, null, null);
    }

    public ResolveResult(Exception exception) {
        this(false, null, exception);
    }

    public ResolveResult(Message message) {
        this(true, message, null);
    }

    public ResolveResult(boolean success, Message message, Exception exception) {
        this.success = success;
        this.message = message;
        this.exception = exception;
    }

    public boolean isSuccess() {
        return success;
    }

    public Message getMessage() {
        return message;
    }

    public Exception getException() { return exception; }
}
