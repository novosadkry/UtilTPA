package cz.novosadkry.UtilTPA.Request;

import java.util.Timer;

public class ExpiringRequest extends Request {
    private Timer timer;
    private final long expiration;

    private boolean expired;

    public ExpiringRequest(RequestPlayer from, RequestPlayer to, long expiration) {
        super(from, to);
        this.expiration = expiration;
    }

    public ExpiringRequest(String from, String to, long expiration) {
        super(from, to);
        this.expiration = expiration;
    }

    public ExpiringRequest(Request request, long expiration) {
        super(request);
        this.expiration = expiration;
    }

    public ExpiringRequest startCountdown() {
        if (timer == null) // Lazy initialization
            this.timer = new Timer();

        timer.schedule(new RequestExpiration(this), expiration);
        return this;
    }

    public ExpiringRequest cancelCountdown() {
        if (timer != null)
            timer.cancel();

        return this;
    }

    public boolean hasExpired() {
        return expired;
    }

    public void setExpired(boolean expired) {
        this.expired = expired;
    }

    @Override
    public void onSent() {
        super.onSent();
        startCountdown();
    }

    @Override
    public void onResolved() {
        super.onResolved();
        cancelCountdown();
    }
}
