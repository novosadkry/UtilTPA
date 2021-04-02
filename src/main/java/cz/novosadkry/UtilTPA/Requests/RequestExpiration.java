package cz.novosadkry.UtilTPA.Requests;

import cz.novosadkry.UtilTPA.Requests.Managers.RequestManager;

public class RequestExpiration implements Runnable {
    ExpiringRequest request;

    public RequestExpiration(ExpiringRequest request) {
        this.request = request;
    }

    public void run() {
        request.setExpired(true);
        RequestManager.getInstance().timeoutRequest(request);
    }
}

