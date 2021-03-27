package cz.novosadkry.UtilTPA.BungeeCord.Transport.Resolvers;

import com.google.common.io.ByteArrayDataInput;
import com.google.common.io.ByteStreams;
import cz.novosadkry.UtilBungee.Transport.Resolvers.IMessageResolver;
import cz.novosadkry.UtilBungee.Transport.Resolvers.ResolveResult;
import cz.novosadkry.UtilTPA.BungeeCord.Transport.Messages.PlayerListMessage;
import cz.novosadkry.UtilTPA.BungeeCord.Transport.Messages.Message;

public class PlayerListMessageResolver implements IMessageResolver {
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
