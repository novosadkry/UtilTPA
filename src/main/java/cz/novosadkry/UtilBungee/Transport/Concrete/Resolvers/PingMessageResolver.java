package cz.novosadkry.UtilBungee.Transport.Concrete.Resolvers;

import com.google.common.io.ByteArrayDataInput;
import com.google.common.io.ByteStreams;
import cz.novosadkry.UtilBungee.Transport.Concrete.Messages.Message;
import cz.novosadkry.UtilBungee.Transport.Concrete.Messages.PingMessage;
import cz.novosadkry.UtilBungee.Transport.Resolvers.IMessageResolver;
import cz.novosadkry.UtilBungee.Transport.Resolvers.ResolveResult;

public class PingMessageResolver implements IMessageResolver {
    @Override
    @SuppressWarnings("UnstableApiUsage")
    public ResolveResult resolve(byte[] data) {
        ByteArrayDataInput input = ByteStreams.newDataInput(data);

        try {
            final String subChannel = input.readUTF();

            if (subChannel.equalsIgnoreCase("PING")) {
                String from = input.readUTF();
                String to = input.readUTF();
                String message = input.readUTF();

                if (input.skipBytes(1) > 0)
                    return new ResolveResult(false);

                Message msg = new PingMessage(from, to, message);
                return new ResolveResult(msg);
            }

            return new ResolveResult(false);
        } catch (Exception e) {
            return new ResolveResult(e);
        }
    }
}
