package cz.novosadkry.UtilTPA.Utils;

public class Pair<K, V> {
    protected K key;
    protected V value;

    public Pair(K key, V value) {
        this.key = key;
        this.value = value;
    }

    public K getKey() {
        return key;
    }

    public V getValue() {
        return value;
    }
    
    public void setValue(V value) {
        this.value = value;
    }
}
