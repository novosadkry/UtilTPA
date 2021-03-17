package cz.novosadkry.UtilTPA.Localization;

import cz.novosadkry.UtilTPA.Utils.Pair;

import java.util.regex.Pattern;

public class PlaceHolder extends Pair<String, Object> {
    public static final Pattern SPLIT_PATTERN;

    static {
        SPLIT_PATTERN = Pattern.compile("(?=\\{)|(?<=\\})");
    }

    public PlaceHolder(String key, Object value) {
        super(key, value);
    }

    public String getHolder() {
        return "{" + getKey() + "}";
    }
}
