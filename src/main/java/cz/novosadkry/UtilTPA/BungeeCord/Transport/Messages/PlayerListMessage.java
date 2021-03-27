package cz.novosadkry.UtilTPA.BungeeCord.Transport.Messages;

import com.google.common.io.ByteArrayDataOutput;
import com.google.common.io.ByteStreams;

public class PlayerListMessage extends Message {
    protected String server;
    protected String[] playerList;

    public String getServer() {
        return server;
    }

    public String[] getPlayerList() {
        return playerList;
    }

    public PlayerListMessage setPlayerList(String[] playerList) {
        this.playerList = playerList;
        return this;
    }

    public PlayerListMessage(String server) {
        this.server = server;
    }

    @Override
    public byte[] toBytes() {
        ByteArrayDataOutput header = ByteStreams.newDataOutput();

        // Write PlayerList header
        header.writeUTF("PlayerList");
        header.writeUTF(server);

        return header.toByteArray();
    }

    @Override
    public String getName() {
        return "PLAYER_LIST";
    }
}
