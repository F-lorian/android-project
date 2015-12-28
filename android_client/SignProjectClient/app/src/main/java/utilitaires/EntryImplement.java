package utilitaires;

/**
 * Created by Axel_2 on 25/12/2015.
 */
import java.util.Map;

public final class EntryImplement<K, V> implements Map.Entry<K, V> {
    private final K key;
    private V value;

    public EntryImplement(K key, V value) {
        this.key = key;
        this.value = value;
    }

    @Override
    public K getKey() {
        return key;
    }

    @Override
    public V getValue() {
        return value;
    }

    @Override
    public V setValue(V value) {
        V old = this.value;
        this.value = value;
        return old;
    }
}