package cz.novosadkry.UtilTPA.BungeeCord.Transport.Resolvers;

public interface IMessageResolver {
    ResolveResult resolve(byte[] data);
}
