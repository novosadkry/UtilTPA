package cz.novosadkry.UtilTPA.BungeeCord.Drivers;

import cz.novosadkry.UtilTPA.BungeeCord.Transport.Messages.Message;
import cz.novosadkry.UtilTPA.Services.IService;
import org.bukkit.plugin.messaging.PluginMessageListener;

import java.util.List;

public interface IBungeeDriver extends PluginMessageListener, IService {
    String getServerName();

    List<String> getPlayerList();

    void sendMessage(Message msg);
}
