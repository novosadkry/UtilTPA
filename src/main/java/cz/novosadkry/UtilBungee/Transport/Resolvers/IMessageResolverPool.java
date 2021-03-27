package cz.novosadkry.UtilBungee.Transport.Resolvers;

import cz.novosadkry.UtilBungee.Transport.Messages.IMessage;

public interface IMessageResolverPool {
    IMessageResolverPool registerResolver(IMessageResolver resolver);

    IMessageResolverPool unregisterResolver(IMessageResolver resolver);

    IMessageResolverPool unregisterResolvers();

    IMessage resolve(byte[] data);
}
