package ru.shapedb.database.statement.base.data;

import ru.shapedb.database.conditions.PreparedCondition;
import ru.shapedb.database.conditions.SqlWhereCondition;
import ru.shapedb.database.statement.base.SqlOperationQuery;
import ru.shapedb.database.statement.base.SqlPreparedOperation;
import ru.shapedb.database.statement.base.SqlStatementBase;
import ru.shapedb.database.statement.result.SqlResult;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class SqlExistsData extends SqlStatementBase implements SqlPreparedOperation, SqlOperationQuery {

    private final String tableName;
    private final List<SqlWhereCondition> whereConditions;
    private SqlResult<Boolean> result;

    public SqlExistsData(String tableName, SqlWhereCondition ... whereConditions) {
        this.tableName = tableName;
        this.whereConditions = List.of(whereConditions);
    }

    public String getTableName() {
        return tableName;
    }

    public List<SqlWhereCondition> getWhereConditions() {
        return whereConditions;
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

    @Override
    public List<PreparedCondition> getPreparedConditions() {
        return new ArrayList<>(whereConditions);
    }
}
