package cz.novosadkry.UtilTPA.Commands.Back;

import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.Map;

public final class BackPersist {
    private static final Map<Player, BackInfo> lastLoc;

    static {
        lastLoc = new HashMap<>();
    }

    public static Map<Player, BackInfo> getLastLoc() {
        return lastLoc;
    }

    private BackPersist() { }
}
