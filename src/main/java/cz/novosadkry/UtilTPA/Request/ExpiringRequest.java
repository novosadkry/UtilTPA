package cz.novosadkry.UtilTPA.Request;

import cz.novosadkry.UtilTPA.Main;
import org.bukkit.Bukkit;
import org.bukkit.scheduler.BukkitTask;

public class ExpiringRequest extends Request {
    private final long expiration;
    private BukkitTask expirationTask;

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
        expirationTask = Bukkit.getScheduler().runTaskLater(
                Main.getInstance(),
                new RequestExpiration(this),
                expiration
        );

        return this;
    }

    public ExpiringRequest cancelCountdown() {
        expirationTask.cancel();
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
