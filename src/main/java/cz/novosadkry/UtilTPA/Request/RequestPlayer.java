package cz.novosadkry.UtilTPA.Request;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

public class RequestPlayer {
    public interface OnRemoteCallback {
        void notify(String name);
    }

    public interface OnLocalCallback {
        void notify(Player player);
    }

    private final String name;

    public RequestPlayer(String name) {
        this.name = name;
    }

    public RequestPlayer(Player player) {
        this.name = player.getName();
    }

    public String getName() {
        return name;
    }

    public Player getPlayer() {
        return Bukkit.getPlayerExact(name);
    }

    public void onRemote(OnRemoteCallback callback) {
        Player player = Bukkit.getPlayerExact(name);

        if (player == null)
            callback.notify(name);
    }

    public void onLocal(OnLocalCallback callback) {
        Player player = Bukkit.getPlayerExact(name);

        if (player != null)
            callback.notify(player);
    }

    public boolean isRemote() {
        return Bukkit.getPlayerExact(name) == null;
    }

    public boolean isLocal() {
        return Bukkit.getPlayerExact(name) != null;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        RequestPlayer p = (RequestPlayer) o;

        return name.equalsIgnoreCase(p.name);
    }

    @Override
    public int hashCode() {
        return name != null ? name.toLowerCase().hashCode() : 0;
    }

    @Override
    public String toString() {
        return getName();
    }
}
