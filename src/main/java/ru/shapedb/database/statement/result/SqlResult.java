package ru.shapedb.database.statement.result;

import java.sql.SQLException;
import java.util.Objects;

public class SqlResult<T> {
    private final Class<T> type;
    private T value;

    public SqlResult(Class<T> type, T value){
        this.value = value;
        this.type = type;
    }

    public SqlResult(Class<T> type){
        this.type = Objects.requireNonNull(type);
    }

    public Class<T> getType() {
        return type;
    }

    public void setRawValue(Object raw) throws SQLException{
        if(raw == null) {
            this.value = null;
            return;
        }

        if(type.isInstance(raw)){
            this.value = type.cast(raw);
        }else{
            throw new ClassCastException("Cannot cast %s to %s".formatted(raw.getClass(), type));
        }
    }

    public void setRawValueUnsafe(T value){
        this.value = value;
    }

    public T get() {
        return value;
    }


}
