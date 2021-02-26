package cz.novosadkry.UtilTPA.BungeeCord.Transport.Listeners;

import cz.novosadkry.UtilTPA.BungeeCord.Transport.Abstract.Message;
import cz.novosadkry.UtilTPA.BungeeCord.Transport.Abstract.MessageListener;
import cz.novosadkry.UtilTPA.BungeeCord.Transport.Messages.ConnectMessage;
import cz.novosadkry.UtilTPA.BungeeCord.Transport.Messages.RequestAcceptMessage;
import cz.novosadkry.UtilTPA.BungeeCord.Transport.Messages.RequestDenyMessage;
import cz.novosadkry.UtilTPA.BungeeCord.Transport.Messages.RequestMessage;
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

        switch (msg.getType()) {
            case REQUEST:
                requestManager.sendRequest(request);
                break;

            case REQUEST_ACCEPT:
                RequestAcceptMessage acceptMsg = (RequestAcceptMessage) msg;
                request.getFrom().onLocal(p -> new ConnectMessage(p, acceptMsg.getServer()).send());
                break;

            case REQUEST_DENY:
                RequestDenyMessage denyMsg = (RequestDenyMessage) msg;
                request.getFrom().onLocal(p -> p.sendMessage(denyMsg.getReason()));
                break;
        }
    }
}
