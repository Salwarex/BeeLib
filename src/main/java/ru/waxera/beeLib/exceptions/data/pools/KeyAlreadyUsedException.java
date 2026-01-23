package ru.waxera.beeLib.exceptions.data.pools;

public class KeyAlreadyUsedException extends RuntimeException{
    public KeyAlreadyUsedException() {
        super("Provided key is already used in IrreplaceableMapStorage!");
    }
}
