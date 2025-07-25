package ru.waxera.beeLib.utils.data.pools.map;

import ru.waxera.beeLib.utils.data.pools.Pool;

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
