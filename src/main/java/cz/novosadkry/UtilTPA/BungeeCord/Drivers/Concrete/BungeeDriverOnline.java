package cz.novosadkry.UtilTPA.BungeeCord.Drivers.Concrete;

import cz.novosadkry.UtilTPA.BungeeCord.Drivers.BungeeDriver;
import cz.novosadkry.UtilTPA.BungeeCord.Transport.Messages.Message;
import cz.novosadkry.UtilTPA.BungeeCord.Transport.Messages.Concrete.GetServerMessage;
import cz.novosadkry.UtilTPA.BungeeCord.Transport.Messages.Concrete.PlayerListMessage;
import cz.novosadkry.UtilTPA.BungeeCord.Transport.Resolvers.MessageResolverPool;
import cz.novosadkry.UtilTPA.Main;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.*;

public class BungeeDriverOnline extends BungeeDriver {
    private final MessageResolverPool resolverPool;

    private String serverName;
    private String[] playerList;

    private final long playerListTick;
    private final long serverNameTick;
    private boolean cancelTasks;

    public BungeeDriverOnline(long playerListTick) {
        this(playerListTick, new MessageResolverPool());
    }

    public BungeeDriverOnline(long playerListTick, MessageResolverPool resolverPool) {
        this.resolverPool = resolverPool;
        playerList = new String[0];

        this.playerListTick = playerListTick;
        this.serverNameTick = 20L;
    }

    @Override
    public String getServerName() {
        return serverName;
    }

    @Override
    public List<String> getPlayerList() {
        return Arrays.asList(playerList);
    }

    @Override
    public void initialize() {
        super.initialize();

        askForServerName();
        refreshPlayerList();
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

        Message msg = resolverPool.resolve(bytes);

        if (msg != null)
            notifyListeners(msg);
    }

    @Override
    public void terminate() {
        super.terminate();
        cancelTasks = true;
    }
}
