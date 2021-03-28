package cz.novosadkry.UtilTPABungee.Transport.Resolvers;

import com.google.common.io.ByteArrayDataInput;
import com.google.common.io.ByteStreams;
import cz.novosadkry.UtilBungee.Transport.Resolvers.IMessageResolver;
import cz.novosadkry.UtilBungee.Transport.Resolvers.ResolveResult;
import cz.novosadkry.UtilTPABungee.Transport.Messages.Message;
import cz.novosadkry.UtilTPABungee.Main;
import cz.novosadkry.UtilTPABungee.Transport.Messages.PingMessage;
import net.md_5.bungee.api.connection.ProxiedPlayer;

public class PingMessageResolver implements IMessageResolver {
    @Override
    @SuppressWarnings("UnstableApiUsage")
    public ResolveResult resolve(byte[] data) {
        ByteArrayDataInput input = ByteStreams.newDataInput(data);

        try {
            final String subChannel = input.readUTF();

            if (subChannel.equalsIgnoreCase("Ping")) {
                String from = input.readUTF();
                String to = input.readUTF();
                String message = input.readUTF();

                if (input.skipBytes(1) > 0)
                    return new ResolveResult(false);

                ProxiedPlayer fromPlayer = Main.getInstance().getProxy().getPlayer(from);
                ProxiedPlayer toPlayer = Main.getInstance().getProxy().getPlayer(to);

                Message msg = new PingMessage(fromPlayer, toPlayer, message);
                return new ResolveResult(msg);
            }

            return new ResolveResult(false);
        } catch (Exception e) {
            return new ResolveResult(e);
        }
    }
}
