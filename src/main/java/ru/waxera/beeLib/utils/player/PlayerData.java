package ru.waxera.beeLib.utils.player;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.permissions.PermissionAttachmentInfo;
import ru.waxera.beeLib.BeeLib;
import ru.waxera.beeLib.utils.message.Message;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.UUID;

public class PlayerData {
    private Player player;
    private final UUID uuid;
    private final String name;
    private String displayName;
    private double hp;
    private Location location;
    private Location respawnLocation;
    private boolean op;
    private long playerTime;

    private List<String> permissions;

    public PlayerData(UUID uuid,
                      String playerName,
                      String displayName,
                      double hp,
                      Location location,
                      Location respawnLocation,
                      boolean op,
                      long playerTime,
                      List<String> permissions){
        this.uuid = uuid;
        this.name = playerName;
        this.displayName = displayName;
        this.hp = hp;
        this.location = location;
        this.respawnLocation = respawnLocation;
        this.op = op;
        this.playerTime = playerTime;
        this.permissions = permissions;
    }
    public PlayerData(Player player){
        if(player == null) {
            this.uuid = null;
            this.name = null;
            this.permissions = null;
            return;
        }
        this.uuid = player.getUniqueId();
        this.name = player.getName();
        this.displayName = player.getDisplayName();
        this.hp = player.getHealthScale();
        this.location = player.getLocation();
        this.respawnLocation = player.getRespawnLocation();
        this.op = player.isOp();
        this.playerTime = player.getPlayerTime();
        this.permissions = permissionsSet(player.getEffectivePermissions());
    }

    public UUID getUniqueId() {
        this.checkPlayer();
        if(this.player != null) return this.player.getUniqueId();
        return this.uuid;
    }

    public String getName() {
        this.checkPlayer();
        if(this.player != null) return this.player.getName();
        return this.name;
    }

    public String getDisplayName() {
        this.checkPlayer();
        if(this.player != null) return this.player.getDisplayName();
        return this.displayName;
    }
    public String getSavedDisplayName(){
        return this.displayName;
    }

    public double getHealthScale() {
        this.checkPlayer();
        if(this.player != null) return this.player.getHealthScale();
        return this.hp;
    }
    public double getSavedHealthScale(){
        return this.hp;
    }

    public Location getLocation() {
        this.checkPlayer();
        if(this.player != null) return this.player.getLocation();
        return this.location;
    }
    public Location getSavedLocation(){
        return this.location;
    }

    public Location getRespawnLocation() {
        this.checkPlayer();
        if(this.player != null) return this.player.getRespawnLocation();
        return this.respawnLocation;
    }
    public Location getSavedRespawnLocation(){
        return this.respawnLocation;
    }

    public boolean isOp() {
        this.checkPlayer();
        if(this.player != null) return this.player.isOp();
        return this.op;
    }
    public boolean isSavedOp(){
        return this.op;
    }

    public long getPlayerTime() {
        this.checkPlayer();
        if(this.player != null) return this.player.getPlayerTime();
        return this.playerTime;
    }
    public long getSavedPlayerTime(){
        return this.playerTime;
    }

    public boolean hasPermission(String permission) {
        this.checkPlayer();
        if(this.player != null) return this.player.hasPermission(permission);
        return this.permissions.contains(permission);
    }

    public List<String> getSavedPermissions() {
        return permissions;
    }


    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    public void setHp(double hp) {
        this.hp = hp;
    }

    public void setLocation(Location location) {
        this.location = location;
    }

    public void setRespawnLocation(Location respawnLocation) {
        this.respawnLocation = respawnLocation;
    }

    public void setOp(boolean op) {
        this.op = op;
    }

    public void setPlayerTime(long playerTime) {
        this.playerTime = playerTime;
    }

    public void setPermissions(Set<PermissionAttachmentInfo> info){
        this.permissions = permissionsSet(info);
    }

    public void save(){
        if(player == null){
            Message.error(null, "The plugin cannot update the player's data because it has not been found!");
            return;
        }
        BeeLib.getDataHandler().savePlayerData(this, false);
    }

    public boolean equalsPermissions(Set<PermissionAttachmentInfo> external){
        List<String> externalPerms = permissionsSet(external);
        List<String> internalPerms = new ArrayList<>(this.permissions);
        for(String perm : externalPerms){
            if(!internalPerms.contains(perm)) return false;
            internalPerms.remove(perm);
        }
        return internalPerms.isEmpty();
    }

    private static List<String> permissionsSet(Set<PermissionAttachmentInfo> info){
        List<String> result = new ArrayList<>();
        for(PermissionAttachmentInfo x : info){
            result.add(x.getPermission());
        }
        return result;
    }

    private void checkPlayer(){
        this.player = getPlayer();
    }
    public Player getPlayer(){ return Bukkit.getPlayer(uuid); }
}
