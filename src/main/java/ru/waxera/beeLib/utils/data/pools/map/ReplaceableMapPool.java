package ru.waxera.beeLib.utils.data.pools.map;

import ru.waxera.beeLib.utils.data.pools.Pool;

/**
 * The implementation of MapPool, which allows you to
 * replace already saved data with newly provided data
 * if a similar key already exists.
 *
 * @see Pool Pool Interface
 * @see MapPool
 * @version 1.0
 * @since v1.3.1
 * @author Salwarex
 */

public abstract class ReplaceableMapPool<K, T> extends MapPool<K, T> implements Pool<K, T> {

    @Override
    public void add(K key, T element){
        if(this.contains(key)){
            this.storage.replace(key, element);
            return;
        }
        this.storage.put(key, element);
    }

}
