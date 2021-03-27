package cz.novosadkry.UtilBungee.Transport.Resolvers;

public interface IMessageResolver {
    IResolveResult resolve(byte[] data);
}
