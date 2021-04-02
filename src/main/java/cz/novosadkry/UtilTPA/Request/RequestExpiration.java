package cz.novosadkry.UtilTPA.Request;

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

