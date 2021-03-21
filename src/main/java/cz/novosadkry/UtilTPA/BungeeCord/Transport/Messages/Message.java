package cz.novosadkry.UtilTPA.BungeeCord.Transport.Messages;

import cz.novosadkry.UtilTPA.BungeeCord.Drivers.BungeeDriver;
import cz.novosadkry.UtilTPA.BungeeCord.Drivers.IBungeeDriver;
import cz.novosadkry.UtilTPA.Main;

public abstract class Message {
    public void send(IBungeeDriver driver) {
        if (driver == null)
            return;

        driver.sendMessage(this);
    }

    public abstract byte[] toBytes();

    public Message on(MessageListener callback) {
        BungeeDriver bungeeDriver = Main.getService(BungeeDriver.class);
        bungeeDriver.registerListener(callback);
        return this;
    }

    public abstract String getName();
}
