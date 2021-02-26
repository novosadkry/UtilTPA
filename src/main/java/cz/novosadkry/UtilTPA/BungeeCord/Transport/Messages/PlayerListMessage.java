package cz.novosadkry.UtilTPA.BungeeCord.Transport.Messages;

import com.google.common.io.ByteArrayDataInput;
import com.google.common.io.ByteArrayDataOutput;
import com.google.common.io.ByteStreams;
import cz.novosadkry.UtilTPA.BungeeCord.Transport.Abstract.Message;
import cz.novosadkry.UtilTPA.BungeeCord.Transport.Abstract.MessageType;
import cz.novosadkry.UtilTPA.Main;
import org.bukkit.Bukkit;

public class PlayerListMessage extends Message {
    protected String server;
    protected String[] playerList;

    public String getServer() {
        return server;
    }

    public String[] getPlayerList() {
        return playerList;
    }

    private PlayerListMessage setPlayerList(String[] playerList) {
        this.playerList = playerList;
        return this;
    }

    public PlayerListMessage(String server) {
        this.server = server;
    }

    @Override
    public MessageType getType() {
        return MessageType.PLAYER_LIST;
    }

    @Override
    public void send() {
        ByteArrayDataOutput header = ByteStreams.newDataOutput();

        // Write PlayerList header
        header.writeUTF("PlayerList");
        header.writeUTF(server);

        Bukkit.getServer().sendPluginMessage(Main.getInstance(), "BungeeCord", header.toByteArray());
    }

    public static Message resolve(ByteArrayDataInput data) {
        String server = data.readUTF();
        String[] playerList = data.readUTF().split(", ");

        return new PlayerListMessage(server).setPlayerList(playerList);
    }
}
