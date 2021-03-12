package cz.novosadkry.UtilTPA.Heads.Cache;

public class HeadCacheItem {
    private final String value;
    private final long ttl;

    public HeadCacheItem(String value, long ttl) {
        this.value = value;
        this.ttl = ttl;
    }

    public String getValue() {
        return value;
    }

    public long getTTL() {
        return ttl;
    }
}
