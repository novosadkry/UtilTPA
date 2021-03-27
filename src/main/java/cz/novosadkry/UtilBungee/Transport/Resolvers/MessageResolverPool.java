package cz.novosadkry.UtilBungee.Transport.Resolvers;

import cz.novosadkry.UtilBungee.Transport.Messages.IMessage;

import java.util.HashMap;
import java.util.Map;

public class MessageResolverPool implements IMessageResolverPool {
    private final Map<Class<?>, IMessageResolver> resolvers;

    public MessageResolverPool() {
        resolvers = new HashMap<>();
    }

    @Override
    public MessageResolverPool registerResolver(IMessageResolver resolver) {
        resolvers.put(resolver.getClass(), resolver);
        return this;
    }

    @Override
    public MessageResolverPool unregisterResolver(IMessageResolver resolver) {
        resolvers.remove(resolver.getClass());
        return this;
    }

    @Override
    public MessageResolverPool unregisterResolvers() {
        resolvers.clear();
        return this;
    }

    /** @return <code>null</code> if not resolved **/
    @Override
    public IMessage resolve(byte[] data) {
        for (IMessageResolver resolver : resolvers.values()) {
            IResolveResult result = resolver.resolve(data);

            if (result.isSuccess())
                return result.getMessage();
        }

        return null;
    }
}
