package ru.waxera.beeLib.utils.data.database;

import java.util.List;

public abstract class DatabaseObject<T extends Enum<T>> {
    private List<T> updated;
    public boolean isUpdated(T check){
        return updated.contains(check);
    }
}
