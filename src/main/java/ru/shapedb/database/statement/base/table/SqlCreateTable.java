package ru.shapedb.database.statement.base.table;

import ru.shapedb.database.conditions.SqlSchemaCondition;
import ru.shapedb.database.statement.base.SqlStatementBase;
import ru.shapedb.database.statement.base.SqlOperationExecute;

import java.util.List;

public class SqlCreateTable extends SqlStatementBase implements SqlOperationExecute {
    private final String tableName;
    private final boolean ifNotExists;
    private final boolean temp;
    private final List<SqlSchemaCondition> conditions;

    public SqlCreateTable(String tableName,
                          boolean ifNotExists,
                          boolean temp,
                          SqlSchemaCondition... conditions) {
        this.tableName = tableName;
        this.ifNotExists = ifNotExists;
        this.temp = temp;
        this.conditions = List.of(conditions);
    }

    public String getTableName() {
        return tableName;
    }

    public boolean isIfNotExists() {
        return ifNotExists;
    }

    public boolean isTemp() {
        return temp;
    }

    public List<SqlSchemaCondition> getConditions() {
        return conditions;
    }
}
