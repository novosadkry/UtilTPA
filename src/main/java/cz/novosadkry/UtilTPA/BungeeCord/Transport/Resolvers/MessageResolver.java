package cz.novosadkry.UtilTPA.BungeeCord.Transport.Resolvers;

public interface MessageResolver {
    ResolveResult resolve(byte[] data);
}
