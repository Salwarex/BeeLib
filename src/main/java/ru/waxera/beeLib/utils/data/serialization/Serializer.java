package ru.waxera.beeLib.utils.data.serialization;

public interface Serializer<T> {
    String serialize(T object);
    T deserialize(String data);
}
