package ru.waxera.beeLib.utils.data.storages;

public interface DataStorage<K, T> {
    void add(K key, T element);
    boolean contains(K key);
    T get(K key);
    void remove(K key);
}
