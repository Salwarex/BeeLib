package ru.waxera.beeLib.utils.data.pools.map;

import ru.waxera.beeLib.exceptions.storages.KeyAlreadyUsedException;
import ru.waxera.beeLib.utils.data.pools.Pool;

public abstract class IrreplaceableMapPool<K, T> extends MapPool<K, T> implements Pool<K, T> {

    @Override
    public void add(K key, T element) {
        if(contains(key)) throw new KeyAlreadyUsedException();
        this.storage.put(key, element);
    }
}
