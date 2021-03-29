package cz.novosadkry.UtilTPABungee.Transport.Messages;

import cz.novosadkry.UtilBungee.Transport.Handlers.IMessageHandler;
import cz.novosadkry.UtilBungee.Transport.Messages.IMessage;
import cz.novosadkry.UtilBungee.Transport.Messages.IMessageListener;
import net.md_5.bungee.api.connection.ProxiedPlayer;

public class TargetMessage implements IMessage {
    private final IMessage message;
    private ProxiedPlayer target;

    public TargetMessage(ProxiedPlayer target, IMessage message) {
        this.target = target;
        this.message = message;
    }

    public ProxiedPlayer getTarget() {
        return target;
    }

    public void setTarget(ProxiedPlayer target) {
        this.target = target;
    }

    @Override
    public void send(IMessageHandler handler) {
        message.send(handler);
    }

    @Override
    public byte[] toBytes() {
        return message.toBytes();
    }

    @Override
    public IMessage on(IMessageListener callback) {
        return message.on(callback);
    }
}
