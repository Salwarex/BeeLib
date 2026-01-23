package ru.waxera.beeLib.utils.player;

import ru.waxera.beeLib.utils.data.pools.map.IrreplaceableMapPool;

import java.util.ArrayList;
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

    public List<String> playerNames(){
        List<String> result = new ArrayList<>();
        for(PlayerData data : this.storage.values()){
            result.add(data.getName());
        }
        return result;
    }

    public PlayerData get(String name){
        for(PlayerData pd : this.storage.values()){
            if(pd.getName().equals(name)) return pd;
        }
        return null;
    }

    public static synchronized PlayerPool getInstance() {
        return instance == null ? instance = new PlayerPool() : instance;
    }
}
