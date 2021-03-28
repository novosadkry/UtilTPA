package cz.novosadkry.UtilTPABungee.Transport.Messages;

import cz.novosadkry.UtilBungee.Transport.Handlers.IMessageHandler;
import cz.novosadkry.UtilTPABungee.Transport.Drivers.PluginMessageHandler;
import net.md_5.bungee.api.connection.ProxiedPlayer;

public abstract class TargetMessage extends Message {
    private ProxiedPlayer target;

    public TargetMessage(ProxiedPlayer target) {
        this.target = target;
    }

    public ProxiedPlayer getTarget() {
        return target;
    }

    public void setTarget(ProxiedPlayer target) {
        this.target = target;
    }

    @Override
    public void send(IMessageHandler handler) {
        if (!(handler instanceof PluginMessageHandler))
            throw new UnsupportedOperationException();

        super.send(handler);

        PluginMessageHandler pHandler = (PluginMessageHandler) handler;
        target.getServer().sendData(pHandler.getChannel(), toBytes());
    }
}
