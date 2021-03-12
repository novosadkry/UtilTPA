package cz.novosadkry.UtilTPA.BungeeCord.Transport.Messages;

import cz.novosadkry.UtilTPA.BungeeCord.Drivers.BungeeDriver;
import cz.novosadkry.UtilTPA.Main;

public abstract class Message {
    public void send() {
        Main.getInstance().getBungeeDriver().sendMessage(this);
    }

    public abstract byte[] toBytes();

    public Message on(MessageListener callback) {
        BungeeDriver bungeeDriver = Main.getInstance().getBungeeDriver();
        bungeeDriver.registerListener(callback);
        return this;
    }

    public abstract String getName();
}
