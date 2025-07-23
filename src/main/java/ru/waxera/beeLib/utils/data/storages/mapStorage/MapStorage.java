package ru.waxera.beeLib.utils.data.storages.mapStorage;

import ru.waxera.beeLib.utils.data.storages.DataStorage;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public abstract class MapStorage<K, T> implements DataStorage<K, T> {
    protected final Map<K, T> storage = new HashMap<>();

    public abstract void setDefaults(List<T> data);

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
