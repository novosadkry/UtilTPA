package cz.novosadkry.UtilTPA.Commands.Back;

import org.bukkit.Location;

public class BackInfo {
    private Location loc;
    private String server;

    public BackInfo(Location loc) {
        this.loc = loc;
    }

    public BackInfo(String server) {
        this.server = server;
    }

    public Location getLoc() {
        return loc;
    }

    public String getServer() {
        return server;
    }

    public void setLoc(Location loc) {
        this.loc = loc;
        this.server = null;
    }

    public void setServer(String server) {
        this.server = server;
        this.loc = null;
    }
}
