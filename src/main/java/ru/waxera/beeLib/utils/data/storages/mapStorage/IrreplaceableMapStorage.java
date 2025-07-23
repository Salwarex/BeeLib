package ru.waxera.beeLib.utils.data.storages.mapStorage;

import ru.waxera.beeLib.exceptions.storages.KeyAlreadyUsedException;
import ru.waxera.beeLib.utils.data.storages.DataStorage;

public abstract class IrreplaceableMapStorage<K, T> extends MapStorage<K, T> implements DataStorage<K, T> {

    @Override
    public void add(K key, T element) {
        if(contains(key)) throw new KeyAlreadyUsedException();
        this.storage.put(key, element);
    }
}
