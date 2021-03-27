package cz.novosadkry.UtilTPABungee;

import cz.novosadkry.UtilTPA.Main;
import org.apache.commons.lang.exception.ExceptionUtils;

public final class Log {
    private Log() { }

    // Exception overloads

    public static void info(Throwable e) {
        info(ExceptionUtils.getStackTrace(e));
    }

    public static void warning(Throwable e) {
        warning(ExceptionUtils.getStackTrace(e));
    }

    public static void severe(Throwable e) {
        severe(ExceptionUtils.getStackTrace(e));
    }

    public static void fine(Throwable e) {
        fine(ExceptionUtils.getStackTrace(e));
    }

    public static void finer(Throwable e) {
        finer(ExceptionUtils.getStackTrace(e));
    }

    public static void finest(Throwable e) {
        finest(ExceptionUtils.getStackTrace(e));
    }

    // String overloads

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
