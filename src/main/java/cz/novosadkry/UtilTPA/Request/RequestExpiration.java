package cz.novosadkry.UtilTPA.Request;

import java.util.TimerTask;

public class RequestExpiration extends TimerTask {
    ExpiringRequest request;

    public RequestExpiration(ExpiringRequest request) {
        this.request = request;
    }

    public void run() {
        request.setExpired(true);
        RequestManager.getInstance().timeoutRequest(request);
    }
}

