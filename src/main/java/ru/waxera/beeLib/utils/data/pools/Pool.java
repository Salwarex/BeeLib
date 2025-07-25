package ru.waxera.beeLib.utils.data.pools;

public interface Pool<K, T> {
    void add(K key, T element);
    boolean contains(K key);
    T get(K key);
    void remove(K key);
}
