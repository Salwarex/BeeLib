package ru.shapedb.database.dialect;

public interface SqlDialect {
    String extract(SqlTranslatable object);
}
