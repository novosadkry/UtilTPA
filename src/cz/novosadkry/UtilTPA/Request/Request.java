package cz.novosadkry.UtilTPA.Request;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.Timer;

public class Request {
    private final String from, to;
    private Timer timer;

    public String getFrom() {
        return from;
    }

    public String getTo() {
        return to;
    }

    public Player getFromPlayer() {
        return Bukkit.getPlayer(from);
    }

    public Player getToPlayer() {
        return Bukkit.getPlayer(to);
    }

    public Request(String from, String to) {
        this.from = from;
        this.to = to;
    }

    public Request(Player from, Player to) {
        this(from.getName(), to.getName());
    }

    public void startCountdown() {
        if (timer == null)
            this.timer = new Timer();

        timer.schedule(new RequestExpiration(this), 20000);
    }

    public void cancelCountdown() {
        if (timer != null)
            timer.cancel();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Request req = (Request) o;

        return from.equals(req.from)
                && to.equals(req.to);
    }

    @Override
    public String toString() {
        return "Request{" +
                "from='" + from + '\'' +
                ", to='" + to + '\'' +
                '}';
    }
}