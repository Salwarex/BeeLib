package ru.waxera.beeLib.utils.player;

import ru.waxera.beeLib.utils.data.storages.map.IrreplaceableMapStorage;

import java.util.List;
import java.util.UUID;

public class PlayerDataStorage extends IrreplaceableMapStorage<UUID, PlayerData> {

    @Override
    public void setDefaults(List<PlayerData> data) {
        for(PlayerData playerData : data){
            this.storage.put(playerData.getUniqueId(), playerData);
        }
    }
}
