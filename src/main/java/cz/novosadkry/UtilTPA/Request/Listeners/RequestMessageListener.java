package cz.novosadkry.UtilTPA.Request.Listeners;

import cz.novosadkry.UtilTPA.BungeeCord.Drivers.IBungeeDriver;
import cz.novosadkry.UtilTPA.BungeeCord.Transport.Messages.Message;
import cz.novosadkry.UtilTPA.BungeeCord.Transport.Messages.MessageListener;
import cz.novosadkry.UtilTPA.BungeeCord.Transport.Messages.Concrete.ConnectMessage;
import cz.novosadkry.UtilTPA.BungeeCord.Transport.Messages.Concrete.RequestAcceptMessage;
import cz.novosadkry.UtilTPA.BungeeCord.Transport.Messages.Concrete.RequestDenyMessage;
import cz.novosadkry.UtilTPA.BungeeCord.Transport.Messages.Concrete.RequestMessage;
import cz.novosadkry.UtilTPA.Main;
import cz.novosadkry.UtilTPA.Request.Request;
import cz.novosadkry.UtilTPA.Request.RequestManager;

public class RequestMessageListener implements MessageListener {
    @Override
    public void onMessage(Message msg) {
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
                        .send(Main.getService(IBungeeDriver.class))
                );
                break;

            case "REQUEST_DENY":
                RequestDenyMessage denyMsg = (RequestDenyMessage) msg;
                request.getFrom().onLocal(p -> p.sendMessage(denyMsg.getReason()));
                break;
        }
    }
}
