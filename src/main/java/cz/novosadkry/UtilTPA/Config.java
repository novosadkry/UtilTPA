package cz.novosadkry.UtilTPA;

public final class Config {
    private Config() { }

    public static boolean getBoolean(String path) {
        return Main.getInstance().getConfig().getBoolean(path);
    }
}
