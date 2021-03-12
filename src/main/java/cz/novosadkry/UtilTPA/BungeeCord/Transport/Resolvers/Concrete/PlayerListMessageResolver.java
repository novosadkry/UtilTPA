package cz.novosadkry.UtilTPA.BungeeCord.Transport.Resolvers.Concrete;

import com.google.common.io.ByteArrayDataInput;
import com.google.common.io.ByteStreams;
import cz.novosadkry.UtilTPA.BungeeCord.Transport.Messages.Concrete.PlayerListMessage;
import cz.novosadkry.UtilTPA.BungeeCord.Transport.Messages.Message;
import cz.novosadkry.UtilTPA.BungeeCord.Transport.Resolvers.MessageResolver;
import cz.novosadkry.UtilTPA.BungeeCord.Transport.Resolvers.ResolveResult;

public class PlayerListMessageResolver implements MessageResolver {
    @Override
    @SuppressWarnings("UnstableApiUsage")
    public ResolveResult resolve(byte[] data) {
        ByteArrayDataInput input = ByteStreams.newDataInput(data);

        try {
            final String subChannel = input.readUTF();

            if (subChannel.equalsIgnoreCase("PlayerList")) {
                String server = input.readUTF();
                String[] playerList = input.readUTF().split(", ");

                if (input.skipBytes(1) > 0)
                    return new ResolveResult(false);

                Message msg = new PlayerListMessage(server).setPlayerList(playerList);
                return new ResolveResult(msg);
            }

            return new ResolveResult(false);
        } catch (Exception e) {
            return new ResolveResult(e);
        }
    }
}
