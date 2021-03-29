package cz.novosadkry.UtilBungee.Transport.Concrete.Resolvers;

import com.google.common.io.ByteArrayDataInput;
import com.google.common.io.ByteStreams;
import cz.novosadkry.UtilBungee.Transport.Concrete.Messages.Message;
import cz.novosadkry.UtilBungee.Transport.Concrete.Messages.RequestAcceptMessage;
import cz.novosadkry.UtilBungee.Transport.Resolvers.IMessageResolver;
import cz.novosadkry.UtilBungee.Transport.Resolvers.ResolveResult;
import cz.novosadkry.UtilTPA.Request.Request;

public class RequestAcceptMessageResolver implements IMessageResolver {
    @Override
    @SuppressWarnings("UnstableApiUsage")
    public ResolveResult resolve(byte[] data) {
        ByteArrayDataInput input = ByteStreams.newDataInput(data);

        try {
            final String subChannel = input.readUTF();

            if (subChannel.equalsIgnoreCase("UtilTPA")) {
                final short length = input.readShort();
                String type = input.readUTF();

                if (!type.equals("REQUEST_ACCEPT"))
                    return new ResolveResult(false);

                String from = input.readUTF();
                String to = input.readUTF();
                String server = input.readUTF();

                if (input.skipBytes(1) > 0)
                    return new ResolveResult(false);

                Message msg = new RequestAcceptMessage(new Request(from, to))
                        .setServer(server);

                return new ResolveResult(msg);
            }

            return new ResolveResult(false);
        } catch (Exception e) {
            return new ResolveResult(e);
        }
    }
}