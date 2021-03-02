package cz.novosadkry.UtilTPA.BungeeCord.Drivers;

import com.google.common.io.ByteArrayDataInput;
import com.google.common.io.ByteStreams;
import cz.novosadkry.UtilTPA.BungeeCord.Transport.Abstract.Message;
import cz.novosadkry.UtilTPA.BungeeCord.Transport.Abstract.MessageListener;
import cz.novosadkry.UtilTPA.BungeeCord.Transport.Messages.GetServerMessage;
import cz.novosadkry.UtilTPA.BungeeCord.Transport.Messages.PlayerListMessage;
import cz.novosadkry.UtilTPA.Main;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class BungeeDriverOnline implements BungeeDriver {
    private final List<MessageListener> listeners;

    private String serverName;
    private String[] playerList;

    private boolean cancelTasks;

    public BungeeDriverOnline() {
        listeners = new ArrayList<>();
        playerList = new String[0];
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
        askForServerName();
        refreshPlayerList();

        Bukkit.getMessenger().registerIncomingPluginChannel(Main.getInstance(), "BungeeCord", this);
        Bukkit.getMessenger().registerOutgoingPluginChannel(Main.getInstance(), "BungeeCord");
    }

    private void askForServerName() {
        Bukkit.getScheduler().runTaskLaterAsynchronously(Main.getInstance(), () -> {
            if (serverName != null || cancelTasks)
                return;

            new GetServerMessage().on(msg -> {
                if (!(msg instanceof GetServerMessage))
                    return true;

                serverName = ((GetServerMessage) msg).getServer();
                return false;
            }).send();

            askForServerName();
        }, 20L);
    }

    private void refreshPlayerList() {
        Bukkit.getScheduler().runTaskLaterAsynchronously(Main.getInstance(), () -> {
            if (cancelTasks)
                return;

            new PlayerListMessage("ALL").on(msg -> {
                if (!(msg instanceof PlayerListMessage))
                    return true;

                playerList = ((PlayerListMessage) msg).getPlayerList();
                return false;
            }).send();

            refreshPlayerList();
        }, 20L);
    }

    @Override
    public void registerListener(MessageListener listener) {
        listeners.add(listener);
    }

    @Override
    public void unregisterListener(MessageListener listener) {
        listeners.remove(listener);
    }

    @Override
    public void unregisterListeners() {
        listeners.clear();
    }

    private void notifyListeners(Message msg) {
        listeners.removeIf(l -> !l.onMessage(msg));
    }

    @Override
    public void sendMessage(Message msg) {
        Bukkit.getServer().sendPluginMessage(Main.getInstance(), "BungeeCord", msg.toBytes());
    }

    @Override
    public void onPluginMessageReceived(String channel, Player player, byte[] bytes) {
        if (!channel.equalsIgnoreCase("BungeeCord"))
            return;

        ByteArrayDataInput data = ByteStreams.newDataInput(bytes);
        Message msg = Message.resolve(data);

        if (msg == null)
            return;

        notifyListeners(msg);
    }

    @Override
    public void terminate() {
        cancelTasks = true;
        unregisterListeners();
    }
}
