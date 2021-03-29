package cz.novosadkry.UtilBungee.Transport.Concrete.Resolvers;

import com.google.common.io.ByteArrayDataInput;
import com.google.common.io.ByteStreams;
import cz.novosadkry.UtilBungee.Transport.Concrete.Messages.Message;
import cz.novosadkry.UtilBungee.Transport.Concrete.Messages.RequestDenyMessage;
import cz.novosadkry.UtilBungee.Transport.Resolvers.IMessageResolver;
import cz.novosadkry.UtilBungee.Transport.Resolvers.ResolveResult;
import cz.novosadkry.UtilTPA.Request.Request;

public class RequestDenyMessageResolver implements IMessageResolver {
    @Override
    @SuppressWarnings("UnstableApiUsage")
    public ResolveResult resolve(byte[] data) {
        ByteArrayDataInput input = ByteStreams.newDataInput(data);

        try {
            final String subChannel = input.readUTF();

            if (subChannel.equalsIgnoreCase("UtilTPA")) {
                final short length = input.readShort();
                String type = input.readUTF();

                if (!type.equals("REQUEST_DENY"))
                    return new ResolveResult(false);

                String from = input.readUTF();
                String to = input.readUTF();
                String reason = input.readUTF();

                if (input.skipBytes(1) > 0)
                    return new ResolveResult(false);

                Message msg = new RequestDenyMessage(new Request(from, to))
                        .setReason(reason);

                return new ResolveResult(msg);
            }

            return new ResolveResult(false);
        } catch (Exception e) {
            return new ResolveResult(e);
        }
    }
}
