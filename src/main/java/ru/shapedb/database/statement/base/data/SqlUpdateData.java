package ru.shapedb.database.statement.base.data;

import ru.shapedb.database.conditions.PreparedCondition;
import ru.shapedb.database.conditions.SqlAttributeCondition;
import ru.shapedb.database.conditions.SqlWhereCondition;
import ru.shapedb.database.statement.base.SqlOperationUpdate;
import ru.shapedb.database.statement.base.SqlPreparedOperation;
import ru.shapedb.database.statement.base.SqlStatementBase;

import java.util.ArrayList;
import java.util.List;

public class SqlUpdateData extends SqlStatementBase implements SqlPreparedOperation, SqlOperationUpdate {
    private final String tableName;
    private List<SqlAttributeCondition> attributesConditions;
    private final List<SqlWhereCondition> whereConditions;

    public SqlUpdateData(String tableName, SqlWhereCondition ... whereConditions){
        this.tableName = tableName;
        this.whereConditions = List.of(whereConditions);
    }

    public SqlUpdateData setDataToChange(SqlAttributeCondition ... attributes){
        this.attributesConditions = List.of(attributes);
        return this;
    }

    public String getTableName() {
        return tableName;
    }

    public List<SqlAttributeCondition> getAttributesConditions() {
        return attributesConditions;
    }

    public List<SqlWhereCondition> getWhereConditions() {
        return whereConditions;
    }

    @Override
    public List<PreparedCondition> getPreparedConditions() {
        List<PreparedCondition> result = new ArrayList<>();
        result.addAll(attributesConditions);
        result.addAll(whereConditions);
        return result;
    }
}
