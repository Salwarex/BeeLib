package ru.shapedb.database.statement.base.column;

import ru.shapedb.database.statement.base.SqlStatementBase;
import ru.shapedb.database.statement.base.SqlOperationExecute;
import ru.shapedb.database.types.DataType;

public class SqlCreateColumn extends SqlStatementBase implements SqlOperationExecute {

    private final String tableName;
    private final String columnName;
    private final DataType type;

    public SqlCreateColumn(String tableName, String columnName, DataType type) {
        this.tableName = tableName;
        this.columnName = columnName;
        this.type = type;
    }

    public String getTableName() {
        return tableName;
    }

    public String getColumnName() {
        return columnName;
    }

    public DataType getType() {
        return type;
    }
}
