package ru.shapedb.database.conditions;

import ru.shapedb.database.types.DataType;

public class SqlSchemaCondition implements SqlCondition{

    private final String name;
    private final DataType type;
    private final boolean primaryKey;
    private final boolean autoIncrement;

    private String foreignTable;
    private String foreignColumn;

    public SqlSchemaCondition(
            String name,
            DataType type,
            boolean primaryKey,
            boolean autoIncrement
    ){
        this.name = name;
        this.type = type;
        this.primaryKey = primaryKey;
        this.autoIncrement = autoIncrement;
    }

    public void setAsForeignKey(String foreignTable, String foreignColumn){
        this.foreignTable = foreignTable;
        this.foreignColumn = foreignColumn;
    }

    public boolean isForeignKey(){
        return (foreignTable != null && foreignColumn != null);
    }

    public String getName() {
        return name;
    }

    public DataType getType() {
        return type;
    }

    public boolean isPrimaryKey() {
        return primaryKey;
    }

    public boolean isAutoIncrement() {
        return autoIncrement;
    }

    public String getForeignTable() {
        return foreignTable;
    }

    public String getForeignColumn() {
        return foreignColumn;
    }
}
