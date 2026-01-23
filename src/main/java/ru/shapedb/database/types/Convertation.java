package ru.shapedb.database.types;

import ru.shapedb.database.statement.result.SqlResult;

@FunctionalInterface
public interface Convertation {
    SqlResult<?> convert(Object raw);
}
