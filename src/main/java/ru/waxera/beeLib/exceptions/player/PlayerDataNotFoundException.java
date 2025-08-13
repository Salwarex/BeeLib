package ru.waxera.beeLib.exceptions.player;

public class PlayerDataNotFoundException extends RuntimeException {
    public PlayerDataNotFoundException() {
        super("PlayerData for this player not found!");
    }
}
