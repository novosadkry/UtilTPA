package cz.novosadkry.UtilTPA.BungeeCord.Listeners;

import cz.novosadkry.UtilBungee.Transport.Concrete.Messages.ConnectMessage;
import cz.novosadkry.UtilBungee.Transport.Concrete.Messages.RequestAcceptMessage;
import cz.novosadkry.UtilBungee.Transport.Concrete.Messages.RequestDenyMessage;
import cz.novosadkry.UtilBungee.Transport.Concrete.Messages.RequestMessage;
import cz.novosadkry.UtilBungee.Transport.Messages.IMessage;
import cz.novosadkry.UtilBungee.Transport.Messages.IMessageListener;
import cz.novosadkry.UtilTPA.BungeeCord.Drivers.BungeeDriver;
import cz.novosadkry.UtilTPA.Main;
import cz.novosadkry.UtilTPA.Requests.Request;
import cz.novosadkry.UtilTPA.Requests.RequestManager;

public class RequestMessageListener implements IMessageListener {
    @Override
    public void onMessage(IMessage msg) {
        if (msg instanceof RequestMessage)
            onMessage((RequestMessage) msg);
    }

    public void onMessage(RequestMessage msg) {
        RequestManager requestManager = RequestManager.getInstance();
        Request request = msg.getRequest();

        switch (msg.getName()) {
            case "REQUEST":
                requestManager.sendRequest(request);
                break;

            case "REQUEST_ACCEPT":
                RequestAcceptMessage acceptMsg = (RequestAcceptMessage) msg;
                request.getFrom().onLocal(p ->
                    new ConnectMessage(p, acceptMsg.getServer())
                        .send(Main.getService(BungeeDriver.class))
                );
                break;

            case "REQUEST_DENY":
                RequestDenyMessage denyMsg = (RequestDenyMessage) msg;
                request.getFrom().onLocal(p -> p.sendMessage(denyMsg.getReason()));
                break;
        }
    }
}
