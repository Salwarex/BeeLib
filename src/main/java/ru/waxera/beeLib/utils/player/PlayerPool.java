package ru.waxera.beeLib.utils.player;

import ru.waxera.beeLib.utils.data.pools.map.IrreplaceableMapPool;

import java.util.List;
import java.util.UUID;

public class PlayerPool extends IrreplaceableMapPool<UUID, PlayerData> {
    private static PlayerPool instance = null;

    private PlayerPool(){}

    @Override
    public void setDefaults(List<PlayerData> data) {
        for(PlayerData playerData : data){
            this.storage.put(playerData.getUniqueId(), playerData);
        }
    }

    public static synchronized PlayerPool getInstance() {
        return instance == null ? instance = new PlayerPool() : instance;
    }
}
