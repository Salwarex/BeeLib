package ru.waxera.beeLib.utils.data.pools.map;

import ru.waxera.beeLib.exceptions.storages.KeyAlreadyUsedException;
import ru.waxera.beeLib.utils.data.pools.Pool;

/**
 * The MapPool implementation, which prohibits replacing already
 * saved data with newly provided data if such a key already exists.
 * In a situation like this, a {@link KeyAlreadyUsedException} will be thrown.
 *
 * @see Pool Pool Interface
 * @see MapPool
 * @version 1.0
 * @since 1.3.1
 * @author Salwarex
 */

public abstract class IrreplaceableMapPool<K, T> extends MapPool<K, T> implements Pool<K, T> {

    @Override
    public void add(K key, T element) {
        if(contains(key)) throw new KeyAlreadyUsedException();
        this.storage.put(key, element);
    }
}
