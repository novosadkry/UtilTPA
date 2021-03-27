package cz.novosadkry.UtilBungee.Transport.Resolvers;

import cz.novosadkry.UtilBungee.Transport.Messages.IMessage;

public interface IResolveResult {
    boolean isSuccess();

    IMessage getMessage();

    Exception getException();
}
