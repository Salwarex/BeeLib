package ru.shapedb.database.statement.result;

import ru.shapedb.database.types.SqlType;

import java.sql.Timestamp;
import java.util.Objects;

public class ColumnSpec<T> {
    final String columnName;
    final Class<T> type;
    final SqlType sqlType;
    final SqlResult<T> result;

    public ColumnSpec(String columnName, Class<T> type) {
        this.columnName = columnName;
        this.type = Objects.requireNonNull(type);
        this.sqlType = guessSqlType(type);
        this.result = new SqlResult<>(type);
    }

    public ColumnSpec(String columnName, SqlType sqlType, Class<T> type) {
        this.columnName = columnName;
        this.type = Objects.requireNonNull(type);
        this.sqlType = Objects.requireNonNull(sqlType);
        this.result = new SqlResult<>(type);
    }

    public String getColumnName() {
        return columnName;
    }

    public Class<T> getType() {
        return type;
    }

    public SqlType getSqlType() {
        return sqlType;
    }

    public SqlResult<T> getResult() {
        return result;
    }

    private static SqlType guessSqlType(Class<?> type) {
        if (type == String.class) return SqlType.VARCHAR;
        if (type == Integer.class || type == int.class) return SqlType.INTEGER;
        if (type == Long.class || type == long.class) return SqlType.BIGINT;
        if (type == Double.class || type == double.class) return SqlType.DOUBLE;
        if (type == Boolean.class || type == boolean.class) return SqlType.BOOLEAN;
        if (type == java.time.LocalDate.class
                || type == java.sql.Date.class) return SqlType.DATE;
        if (type == java.time.LocalDateTime.class
                || type == Timestamp.class) return SqlType.TIMESTAMP;
        if (type == byte[].class) return SqlType.VARBINARY;
        return SqlType.VARCHAR;
    }
}
