package ru.waxera.beeLib.utils.data;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Player;
import ru.waxera.beeLib.BeeLib;
import ru.waxera.beeLib.utils.data.database.Database;
import ru.waxera.beeLib.utils.data.database.DatabaseType;
import ru.waxera.beeLib.utils.data.serialization.ObjectSerializer;
import ru.waxera.beeLib.utils.player.PlayerData;
import ru.waxera.beeLib.utils.player.PlayerDataStorage;
import ru.waxera.beeLib.utils.preferences.beeLibPrefs.BeeLibPreferencesKeys;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

public class LocalDataHandler {
    private static Database database;
    public LocalDataHandler(){
        database = new Database(DatabaseType.SQLITE, BeeLib.getInstance().getDataFolder().getAbsolutePath() +
                "/database.db", null, null);
        if((Boolean) BeeLib.getPreferences().get(BeeLibPreferencesKeys.ALLOW_PLAYER_DATA_KEEPING)) {
            database.createTable("players_data",
                    "id INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL," +
                            "unique_id VARCHAR(36) NOT NULL UNIQUE, " +
                            "player_name VARCHAR(32) NOT NULL UNIQUE," +
                            "display_name VARCHAR(32) UNIQUE, " +
                            "hp REAL, " +
                            "world_quit VARCHAR(32), " +
                            "x_quit INTEGER, " +
                            "y_quit INTEGER, " +
                            "z_quit INTEGER, " +
                            "world_respawn VARCHAR(32), " +
                            "x_respawn INTEGER, " +
                            "y_respawn INTEGER, " +
                            "z_respawn INTEGER, " +
                            "op INTEGER DEFAULT 0, " +
                            "player_time INTEGER DEFAULT 0," +
                            "permission TEXT");
        }
    }

    public void savePlayerData(PlayerData playerData, boolean first){
        if(!(Boolean) BeeLib.getPreferences().get(BeeLibPreferencesKeys.ALLOW_PLAYER_DATA_KEEPING)) return;
        ObjectSerializer<List<String>> permSrz = new ObjectSerializer<>();
        if(first){
            Location last = playerData.getLocation();
            Location respawn = playerData.getRespawnLocation();

            database.insert("players_data", "unique_id, player_name, display_name, hp, world_quit," +
                            "x_quit, y_quit, z_quit, world_respawn, x_respawn, y_respawn, z_respawn, permission",
                    playerData.getUniqueId(), playerData.getName(), playerData.getHealthScale(),
                    last.getWorld(), last.getBlockX(), last.getBlockY(), last.getBlockZ(), respawn.getWorld(),
                    respawn.getBlockX(), respawn.getBlockY(), respawn.getBlockZ(), permSrz.serialize(playerData.getSavedPermissions()));
        }
        else{
            Player player = playerData.getPlayer();
            if(player == null) return;
            HashMap<String, Object> where = new HashMap<>();
            where.put("uuid", player.getUniqueId());

            if(!playerData.getSavedDisplayName().equalsIgnoreCase(playerData.getDisplayName())){
                database.updateData("players_data",
                        "display_name", player.getDisplayName(), where, new boolean[]{true});
                playerData.setDisplayName(player.getDisplayName());
            }
            if(playerData.getSavedHealthScale() != player.getHealthScale()){
                database.updateData("players_data",
                        "hp", player.getHealthScale(), where, new boolean[]{true});
                playerData.setHp(player.getHealthScale());
            }
            if(!playerData.getSavedLocation().equals(player.getLocation())){
                Location loc = player.getLocation();
                database.updateData("players_data",
                        "world_quit", loc.getWorld().getName(), where, new boolean[]{true});
                database.updateData("players_data",
                        "x_quit", loc.getBlockX(), where, new boolean[]{true});
                database.updateData("players_data",
                        "y_quit", loc.getBlockY(), where, new boolean[]{true});
                database.updateData("players_data",
                        "z_quit", loc.getBlockZ(), where, new boolean[]{true});
                playerData.setLocation(loc);
            }
            if(!playerData.getSavedRespawnLocation().equals(player.getRespawnLocation())){
                Location loc = player.getRespawnLocation();
                database.updateData("players_data",
                        "world_respawn", loc.getWorld().getName(), where, new boolean[]{true});
                database.updateData("players_data",
                        "x_respawn", loc.getBlockX(), where, new boolean[]{true});
                database.updateData("players_data",
                        "y_respawn", loc.getBlockY(), where, new boolean[]{true});
                database.updateData("players_data",
                        "z_respawn", loc.getBlockZ(), where, new boolean[]{true});
                playerData.setRespawnLocation(loc);
            }
            if(playerData.isSavedOp() != player.isOp()){
                database.updateData("players_data",
                        "op", (player.isOp() ? 1 : 0), where, new boolean[]{true});
                playerData.setOp(player.isOp());
            }
            if(playerData.equalsPermissions(player.getEffectivePermissions())){
                database.updateData("players_data",
                        "permission", permSrz.serialize(playerData.getSavedPermissions()), where, new boolean[]{true});
                playerData.setPermissions(player.getEffectivePermissions());
            }
        }

    }

    public PlayerDataStorage getPlayerDataStorage(){
        if(!(Boolean) BeeLib.getPreferences().get(BeeLibPreferencesKeys.ALLOW_PLAYER_DATA_KEEPING)) return null;
        ArrayList<ArrayList<Object>> dataset = database.getDataObjects("*",
                new String[]{"unique_id", "player_name", "display_name", "hp", "world_quit",
                        "x_quit", "y_quit", "z_quit", "world_respawn", "x_respawn", "y_respawn",
                        "z_respawn", "op", "player_time", "permission"},
                "players_data", null, null);

        List<PlayerData> data = new ArrayList<>();

        if(dataset != null){
            for (ArrayList<Object> object : dataset){
                UUID uuid = UUID.fromString(((String) object.get(0)));
                String playerName = ((String) object.get(1));
                String displayName = ((String) object.get(2));
                double health = (double) object.get(3);
                World world = Bukkit.getWorld(((String) object.get(4)));
                int x = ((int) object.get(5));
                int y = ((int) object.get(6));
                int z = ((int) object.get(7));
                Location location = new Location(world, x, y, z);
                World world_r = Bukkit.getWorld(((String) object.get(8)));
                int x_r = ((int) object.get(9));
                int y_r = ((int) object.get(10));
                int z_r = ((int) object.get(11));
                Location location_r = new Location(world_r, x_r, y_r, z_r);
                boolean op = ((int) object.get(12) != 0);
                long playerTime = (long) object.get(13);
                String permissionsSerialized = (String) object.get(14);
                List<String> permissionsDeserialized = (new ObjectSerializer<ArrayList<String>>()).deserialize(permissionsSerialized);

                PlayerData playerData = new PlayerData(
                        uuid,
                        playerName,
                        displayName,
                        health,
                        location,
                        location_r,
                        op,
                        playerTime,
                        permissionsDeserialized
                );

                data.add(playerData);
            }
        }
        PlayerDataStorage storage = new PlayerDataStorage();
        storage.setDefaults(data);

        return storage;
    }
}
