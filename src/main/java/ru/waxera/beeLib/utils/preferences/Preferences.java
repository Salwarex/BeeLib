package ru.waxera.beeLib.utils.preferences;

import org.bukkit.configuration.file.FileConfiguration;

public abstract class Preferences<T extends Enum<T> & ConfigKeys> {
    private final FileConfiguration config;
    public Preferences(FileConfiguration config){
        this.config = config;
    }

    public Object get(T key){
        String path = key.getPath();
        return config.get(path, null);
    }
}
