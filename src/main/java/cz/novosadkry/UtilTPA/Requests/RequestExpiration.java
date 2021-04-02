package cz.novosadkry.UtilTPA.Requests;

import cz.novosadkry.UtilTPA.Main;
import cz.novosadkry.UtilTPA.Requests.Managers.LocalRequestManager;

public class RequestExpiration implements Runnable {
    ExpiringRequest request;

    public RequestExpiration(ExpiringRequest request) {
        this.request = request;
    }

    public void run() {
        request.setExpired(true);
        Main.getService(LocalRequestManager.class).timeoutRequest(request);
    }
}

