package ru.waxera.beeLib.utils.data.database;

import java.util.List;

public abstract class DatabaseObject<T extends Enum<T>> {
    private List<T> updated;
    public boolean isUpdated(T variable){
        return updated.contains(variable);
    }
    protected void setUpdated(T variable){
        updated.add(variable);
    }
    public void clearUpdated(){
        updated.clear();
    }
}
