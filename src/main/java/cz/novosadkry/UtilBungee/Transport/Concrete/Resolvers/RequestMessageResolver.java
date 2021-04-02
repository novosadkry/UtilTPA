package cz.novosadkry.UtilBungee.Transport.Concrete.Resolvers;

import com.google.common.io.ByteArrayDataInput;
import com.google.common.io.ByteStreams;
import cz.novosadkry.UtilBungee.Transport.Concrete.Messages.Message;
import cz.novosadkry.UtilBungee.Transport.Concrete.Messages.RequestMessage;
import cz.novosadkry.UtilBungee.Transport.Resolvers.IMessageResolver;
import cz.novosadkry.UtilBungee.Transport.Resolvers.ResolveResult;
import cz.novosadkry.UtilTPA.Requests.Request;

public class RequestMessageResolver implements IMessageResolver {
    @Override
    @SuppressWarnings("UnstableApiUsage")
    public ResolveResult resolve(byte[] data) {
        ByteArrayDataInput input = ByteStreams.newDataInput(data);

        try {
            final String subChannel = input.readUTF();

            if (subChannel.equalsIgnoreCase("UtilTPA")) {
                final short length = input.readShort();
                String type = input.readUTF();

                if (!type.equals("REQUEST"))
                    return new ResolveResult(false);

                String from = input.readUTF();
                String to = input.readUTF();

                if (input.skipBytes(1) > 0)
                    return new ResolveResult(false);

                Message msg = new RequestMessage(new Request(from, to));
                return new ResolveResult(msg);
            }

            return new ResolveResult(false);
        } catch (Exception e) {
            return new ResolveResult(e);
        }
    }
}
