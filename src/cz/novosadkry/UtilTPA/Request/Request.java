package cz.novosadkry.UtilTPA.Request;

import java.util.Timer;

import org.bukkit.entity.Player;

public class Request {
    Player from, to;
    Timer timer;

    public Player getFrom() {
        return from;
    }

    public Player getTo() {
        return to;
    }

    public Request(Player from, Player to) {
        this.from = from;
        this.to = to;
        this.timer = new Timer();
    }

    public void startCountdown() {
        timer.schedule(new RequestExpiration(this), 20000);
    }

    public void cancelCountdown() {
        timer.cancel();
    }

    @Override
    public boolean equals(Object obj) {
        return from == ((Request)obj).from
                && to == ((Request)obj).to;
    }
}