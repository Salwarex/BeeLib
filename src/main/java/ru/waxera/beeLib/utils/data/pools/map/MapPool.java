package ru.waxera.beeLib.utils.data.pools.map;

import ru.waxera.beeLib.utils.data.pools.Pool;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * An implementation of the Pool interface based on the use of the Map collection.
 *
 * @see Pool Pool Interface
 * @version 1.0
 * @since v1.3.1
 * @author Salwarex
 */

public abstract class MapPool<K, T> implements Pool<K, T> {
    protected final Map<K, T> storage = new HashMap<>();

    /**
     * Adding objects to the pool during initialization.
     *
     * @param data A map that containing keys and provided objects.
     */
    public void setDefaults(Map<K, T> data){
        if(data == null) return;
        for(K key : data.keySet()){
            storage.put(key, data.get(key));
        }
    }

    /**
     * Alternative way to adding objects to the pool during initialization
     *
     * <p>
     * The setDefault(List) method is responsible for supplying the values of objects
     * that are added to the pool and contain objects used as keywords inside. It is
     * implemented to provide encapsulation. It is not abstract, because setDefaults(Map)
     * is used by the standard.
     * </p
     *
     * @param data A list of objects containing keys inside as initialized fields
     *
     * @author Salwarex
     */
    public void setDefaults(List<T> data){}

    @Override
    public void add(K key, T element){}

    @Override
    public boolean contains(K key) {
        return this.storage.containsKey(key);
    }

    @Override
    public T get(K key) {
        if(contains(key)) return this.storage.get(key);
        return null;
    }

    @Override
    public void remove(K key) {
        if(!contains(key)) return;
        this.storage.remove(key);
    }
}
