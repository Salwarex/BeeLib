package ru.waxera.beeLib.utils.player;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import ru.waxera.beeLib.BeeLib;

import java.util.UUID;

public class PlayerDataListener implements Listener {
    private final PlayerPool storage = BeeLib.getPlayerDataStorage();

    @EventHandler
    public void onJoin(PlayerJoinEvent e){
        this.checking(e.getPlayer());
    }

    @EventHandler
    public void onQuit(PlayerQuitEvent e){
        PlayerData data = this.checking(e.getPlayer());
        assert data != null;
        data.save();
    }

    private PlayerData checking(Player player){
        if(this.storage == null) return null;
        UUID uuid = player.getUniqueId();

        PlayerData data = this.storage.get(uuid);
        if(data == null){
            data = new PlayerData(player);
            this.storage.add(uuid, data);
            BeeLib.getDataHandler().savePlayerData(data, true);
        }
        return data;
    }

}
