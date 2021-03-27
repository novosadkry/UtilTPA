package cz.novosadkry.UtilBungee.Transport.Resolvers;

import cz.novosadkry.UtilBungee.Transport.Messages.IMessage;

public class ResolveResult implements IResolveResult {
    private final boolean success;
    private final IMessage message;
    private final Exception exception;

    public ResolveResult(boolean success) {
        this(success, null, null);
    }

    public ResolveResult(Exception exception) {
        this(false, null, exception);
    }

    public ResolveResult(IMessage message) {
        this(true, message, null);
    }

    public ResolveResult(boolean success, IMessage message, Exception exception) {
        this.success = success;
        this.message = message;
        this.exception = exception;
    }

    @Override
    public boolean isSuccess() {
        return success;
    }

    @Override
    public IMessage getMessage() {
        return message;
    }

    @Override
    public Exception getException() { return exception; }
}
