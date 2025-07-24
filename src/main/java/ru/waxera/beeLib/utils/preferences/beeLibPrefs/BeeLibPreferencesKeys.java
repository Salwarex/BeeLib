package ru.waxera.beeLib.utils.preferences.beeLibPrefs;

import ru.waxera.beeLib.utils.preferences.ConfigKeys;

public enum BeeLibPreferencesKeys implements ConfigKeys {
    ALLOW_PLAYER_DATA_KEEPING("services.player-data-storage");

    final String path;
    BeeLibPreferencesKeys(String path){
        this.path = path;
    }

    @Override
    public String getPath(){
        return this.path;
    }
}
