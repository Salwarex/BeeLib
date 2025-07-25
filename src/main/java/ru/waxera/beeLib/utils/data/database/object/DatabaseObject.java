package ru.waxera.beeLib.utils.data.database.object;

import java.util.List;

public abstract class DatabaseObject<T extends Enum<T>> {
    private List<T> updated;
    public boolean isUpdated(T variable){
        return updated.contains(variable);
    }
    protected void setUpdated(T variable){
        this.updated.add(variable);
    }
    public void clearUpdated(){
        this.updated.clear();
    }
}
