package ru.waxera.beeLib.exceptions.storages;

public class KeyAlreadyUsedException extends RuntimeException{
    public KeyAlreadyUsedException() {
        super("Provided key is already used in IrreplaceableMapStorage!");
    }
}
