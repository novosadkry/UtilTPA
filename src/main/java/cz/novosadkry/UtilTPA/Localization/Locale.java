package cz.novosadkry.UtilTPA.Localization;

import cz.novosadkry.UtilTPA.Config;
import cz.novosadkry.UtilTPA.Log;
import net.md_5.bungee.api.chat.BaseComponent;
import net.md_5.bungee.api.chat.ComponentBuilder;
import org.bukkit.configuration.file.FileConfiguration;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public final class Locale {
    private static Locale instance;

    private final Map<String, String> translationMap;

    private Locale() {
        translationMap = new HashMap<>();
    }

    public void load() {
        FileConfiguration tlFile = Config.loadEmbedded("locale.yml");

        Map<String, Object> textMap = tlFile.getValues(true);
        textMap.forEach((k, v) -> translationMap.put(k, v.toString()));
    }

    public String translate(String key) {
        String text = translationMap.get(key);

        if (text == null) {
            text = "Localization key '" + key + "' is empty! Please define one in locale.yml file.";
            Log.warning(text);
        }

        return text;
    }

    public String translate(String key, PlaceHolder... values) {
        String text = translate(key);

        for (PlaceHolder pair : values) {
            Object value = pair.getValue();

            if (!(value instanceof BaseComponent))
                text = text.replace(pair.getHolder(), value.toString());
        }

        return text;
    }

    public BaseComponent[] translateWithComponents(String key, PlaceHolder... values) {
        String text = translate(key, values);
        String[] split = PlaceHolder.SPLIT_PATTERN.split(text);

        ComponentBuilder builder = new ComponentBuilder();

        for (String part : split) {
            if (part.startsWith("{")) {
                Optional<PlaceHolder> placeHolder = Arrays.stream(values)
                        .filter(ph -> ph.getHolder().equals(part))
                        .findFirst();

                if (placeHolder.isPresent()) {
                    Object value = placeHolder.get().getValue();

                    if (value instanceof BaseComponent)
                        builder.append((BaseComponent) value);

                    continue;
                }
            }

            builder.append(part);
        }

        return builder.create();
    }

    /**
     * Gets a translated text from the localization file
     * @param key corresponding YAML key
     * @return Translated text
     */
    public static String tl(String key) {
        return getInstance().translate(key);
    }

    /**
     * Gets a translated text from the localization file
     * @param key corresponding YAML key
     * @param values placeholders for dynamic values
     * @return Translated text with filled placeholders
     */
    public static String tl(String key, PlaceHolder... values) {
        return getInstance().translate(key, values);
    }

    /**
     * Gets a translated text from the localization file with ChatComponents
     * @param key corresponding YAML key
     * @param values placeholders for dynamic values
     * @return Translated text with filled placeholders
     */
    public static BaseComponent[] tlc(String key, PlaceHolder... values) {
        return getInstance().translateWithComponents(key, values);
    }

    public static Locale getInstance() {
        if (instance == null)
            instance = new Locale();

        return instance;
    }
}
