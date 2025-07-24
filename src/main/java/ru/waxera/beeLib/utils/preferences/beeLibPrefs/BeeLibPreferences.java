package ru.waxera.beeLib.utils.preferences.beeLibPrefs;

import org.bukkit.configuration.file.FileConfiguration;
import ru.waxera.beeLib.utils.preferences.Preferences;

public class BeeLibPreferences extends Preferences<BeeLibPreferencesKeys> {
    public BeeLibPreferences(FileConfiguration config) {
        super(config);
    }
}
