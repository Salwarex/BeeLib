package ru.shapedb.database.statement.base.column;

import ru.shapedb.database.statement.base.SqlStatementBase;
import ru.shapedb.database.statement.base.SqlOperationQuery;
import ru.shapedb.database.statement.result.SqlResult;

import java.sql.ResultSet;
import java.sql.SQLException;

public class SqlExistsColumn extends SqlStatementBase implements SqlOperationQuery {
    private final String tableName;
    private final String columnName;
    private SqlResult<Boolean> result;

    public SqlExistsColumn(String tableName, String columnName) {
        this.tableName = tableName;
        this.columnName = columnName;
    }

    public String getTableName() {
        return tableName;
    }

    public String getColumnName() {
        return columnName;
    }

    @Override
    public SqlResult<Boolean> getResult() {
        return result;
    }

    @Override
    public void provideResult(ResultSet rawResult) {
        if(result != null) throw new RuntimeException("Result already provided!");
        if(rawResult == null) throw new IllegalArgumentException("Raw result is null!");

        boolean exists = false;

        try{
            if(rawResult.next()){
                exists = rawResult.getBoolean(1);
            }
        }catch (SQLException e){
            throw new RuntimeException("SQL ResultSet data receiving error!");
        }

        result = new SqlResult<>(Boolean.class, exists);
    }
}
