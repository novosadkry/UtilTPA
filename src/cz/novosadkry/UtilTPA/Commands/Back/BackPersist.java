package cz.novosadkry.UtilTPA.Commands.Back;

import org.bukkit.Location;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.Map;

public final class BackPersist {
    public static final Map<Player, Location> lastLoc;

    static {
        lastLoc = new HashMap<>();
    }

    private BackPersist() { }
}
