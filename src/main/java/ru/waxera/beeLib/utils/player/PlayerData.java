package ru.waxera.beeLib.utils.player;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.permissions.PermissionAttachmentInfo;
import ru.waxera.beeLib.BeeLib;

import java.time.LocalDateTime;
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
    private LocalDateTime firstSession;
    private LocalDateTime lastSession;

    private boolean sessionStateChanged = false;

    private List<String> permissions;

    public PlayerData(UUID uuid,
                      String playerName,
                      String displayName,
                      double hp,
                      Location location,
                      Location respawnLocation,
                      boolean op,
                      long playerTime,
                      List<String> permissions,
                      LocalDateTime firstSession,
                      LocalDateTime lastSession){
        this.uuid = uuid;
        this.name = playerName;
        this.displayName = displayName;
        this.hp = hp;
        this.location = location;
        this.respawnLocation = respawnLocation;
        this.op = op;
        this.playerTime = playerTime;
        this.permissions = permissions;
        this.firstSession = firstSession;
        this.lastSession = lastSession;
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
        this.firstSession = LocalDateTime.now();
        this.lastSession = LocalDateTime.now();
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

    public boolean isSessionStateChanged(){ return this.sessionStateChanged; }

    public boolean hasPermission(String permission) {
        this.checkPlayer();
        if(this.player != null) return this.player.hasPermission(permission);
        return this.permissions.contains(permission);
    }

    public void addPermission(String perm){
        if(!permissions.contains(perm)) permissions.add(perm);
        save(List.of(PlayerSaveFlag.FORCE_PERMISSION_CHANGE));
    }
    public void removePermission(String perm){
        permissions.remove(perm);
        save(List.of(PlayerSaveFlag.FORCE_PERMISSION_CHANGE));
    }

    public List<String> getSavedPermissions() {
        return permissions;
    }

    public LocalDateTime getFirstSession() {
        return firstSession;
    }

    public LocalDateTime getLastSession() {
        return lastSession;
    }

    public void updateLastSession(){
        this.lastSession = LocalDateTime.now();
    }

    public void setPlayer(Player player){
        this.player = player;
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

    public void setSessionStateChanged(boolean b){ this.sessionStateChanged = b; }

    public void save(List<PlayerSaveFlag> flags){
        BeeLib.getDataHandler().savePlayerData(this, flags);
    }

    public boolean equalsPermissions(Set<PermissionAttachmentInfo> external){
        List<String> externalPerms = permissionsSet(external);
        List<String> internalPerms = new ArrayList<>(this.permissions);
        for(String perm : externalPerms){
            if(!internalPerms.contains(perm)) { return false; }
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
