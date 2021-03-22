package cz.novosadkry.UtilTPA.BungeeCord.Transport.Resolvers;

import cz.novosadkry.UtilTPA.BungeeCord.Transport.Messages.Message;
import cz.novosadkry.UtilTPA.Log;

import java.util.HashMap;
import java.util.Map;

public class MessageResolverPool {
    private final Map<Class<?>, IMessageResolver> resolvers;

    public MessageResolverPool() {
        resolvers = new HashMap<>();
    }

    public MessageResolverPool registerResolver(IMessageResolver resolver) {
        if (resolvers.put(resolver.getClass(), resolver) == null)
            Log.fine(getClass().getSimpleName() + " : Register resolver of type " + resolver.getClass().getName());

        return this;
    }

    public MessageResolverPool unregisterResolver(IMessageResolver resolver) {
        resolvers.remove(resolver.getClass());
        return this;
    }

    public MessageResolverPool unregisterResolvers() {
        resolvers.clear();
        return this;
    }

    /** @return <code>null</code> if not resolved **/
    public Message resolve(byte[] data) {
        for (IMessageResolver resolver : resolvers.values()) {
            ResolveResult result = resolver.resolve(data);

            if (result.isSuccess())
                return result.getMessage();
        }

        return null;
    }
}
