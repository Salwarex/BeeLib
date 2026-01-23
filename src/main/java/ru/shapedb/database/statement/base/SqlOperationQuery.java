package ru.shapedb.database.statement.base;

import ru.shapedb.database.statement.result.SqlResult;

import java.sql.ResultSet;

public interface SqlOperationQuery {
    SqlResult<?> getResult();
    void provideResult(ResultSet rawResult);
}
