package cz.novosadkry.UtilTPA.Request;

import java.util.TimerTask;

public class RequestExpiration extends TimerTask {
    Request request;

    public RequestExpiration(Request request) {
        this.request = request;
    }

    public void run() {
        RequestManager.getInstance().timeoutRequest(request);
    }
}

