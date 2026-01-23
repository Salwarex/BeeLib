package ru.waxera.beeLib.utils.data.serialization;

public interface Serialization<T> {
    String serialize(T object);
    T deserialize(String data);
}
