package ru.waxera.beeLib.utils.data.storages.map;

import ru.waxera.beeLib.utils.data.storages.DataStorage;

public abstract class ReplaceableMapStorage<K, T> extends MapStorage<K, T> implements DataStorage<K, T> {

    @Override
    public void add(K key, T element){
        if(this.contains(key)){
            this.storage.replace(key, element);
            return;
        }
        this.storage.put(key, element);
    }

}
