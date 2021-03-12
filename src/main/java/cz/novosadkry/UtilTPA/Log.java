package cz.novosadkry.UtilTPA;

public final class Log {
    private Log() { }

    public static void info(String msg) {
        Main.getInstance().getLogger().info(msg);
    }

    public static void warning(String msg) {
        Main.getInstance().getLogger().warning(msg);
    }

    public static void severe(String msg) {
        Main.getInstance().getLogger().severe(msg);
    }

    public static void fine(String msg) {
        Main.getInstance().getLogger().fine(msg);
    }

    public static void finer(String msg) {
        Main.getInstance().getLogger().finer(msg);
    }

    public static void finest(String msg) {
        Main.getInstance().getLogger().finest(msg);
    }
}
