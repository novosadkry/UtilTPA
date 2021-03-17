package cz.novosadkry.UtilTPA;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;

public final class Config {
    private Config() { }

    public static int getInt(String path) {
        return Main.getInstance().getConfig().getInt(path);
    }

    public static String getString(String path) {
        return Main.getInstance().getConfig().getString(path);
    }

    public static boolean getBoolean(String path) {
        return Main.getInstance().getConfig().getBoolean(path);
    }

    public static FileConfiguration loadEmbedded(String name) {
        File file = new File(Main.getInstance().getDataFolder(), name);

        if (!file.exists())
            Main.getInstance().saveResource(name, false);

        FileConfiguration config = new YamlConfiguration();

        try {
            config.load(file);
        } catch (Exception e) {
            Log.severe(e);
        }

        return config;
    }
}
