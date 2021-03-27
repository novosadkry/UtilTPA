package cz.novosadkry.UtilTPA.BungeeCord.Drivers;

import cz.novosadkry.UtilBungee.Transport.Messages.IMessage;
import cz.novosadkry.UtilBungee.Transport.Resolvers.IMessageResolverPool;
import cz.novosadkry.UtilBungee.Transport.Resolvers.MessageResolverPool;
import cz.novosadkry.UtilTPA.BungeeCord.Transport.Messages.GetServerMessage;
import cz.novosadkry.UtilTPA.BungeeCord.Transport.Messages.PlayerListMessage;
import cz.novosadkry.UtilTPA.Main;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.*;

public class BungeeDriverOnline extends BungeeDriver {
    private String serverName;
    private String[] playerList;

    private final long playerListTick;
    private final long serverNameTick;
    private boolean cancelTasks;

    public BungeeDriverOnline(long playerListTick) {
        this(playerListTick, new MessageResolverPool());
    }

    public BungeeDriverOnline(long playerListTick, IMessageResolverPool resolverPool) {
        super(resolverPool);

        playerList = new String[0];

        this.playerListTick = playerListTick;
        this.serverNameTick = 20L;
    }

    public String getServerName() {
        return serverName;
    }

    public List<String> getPlayerList() {
        return Arrays.asList(playerList);
    }

    @Override
    public void initialize() {
        Bukkit.getMessenger().registerIncomingPluginChannel(Main.getInstance(), "BungeeCord", this);
        Bukkit.getMessenger().registerOutgoingPluginChannel(Main.getInstance(), "BungeeCord");

        askForServerName();
        refreshPlayerList();
    }

    @Override
    public void sendMessage(IMessage msg) {
        Bukkit.getServer().sendPluginMessage(Main.getInstance(), "BungeeCord", msg.toBytes());
    }

    private void askForServerName() {
        Bukkit.getScheduler().runTaskLaterAsynchronously(Main.getInstance(), () -> {
            if (serverName != null || cancelTasks)
                return;

            new GetServerMessage().on(msg -> {
                if (!(msg instanceof GetServerMessage))
                    return;

                serverName = ((GetServerMessage) msg).getServer();
            }).send(this);

            askForServerName();
        }, serverNameTick);
    }

    private void refreshPlayerList() {
        Bukkit.getScheduler().runTaskLaterAsynchronously(Main.getInstance(), () -> {
            if (cancelTasks)
                return;

            new PlayerListMessage("ALL").on(msg -> {
                if (!(msg instanceof PlayerListMessage))
                    return;

                playerList = ((PlayerListMessage) msg).getPlayerList();
            }).send(this);

            refreshPlayerList();
        }, playerListTick);
    }

    @Override
    public void onPluginMessageReceived(String channel, Player player, byte[] bytes) {
        if (!channel.equalsIgnoreCase("BungeeCord"))
            return;

        handleData(bytes);
    }

    @Override
    public void terminate() {
        unregisterListeners();
        cancelTasks = true;
    }
}
