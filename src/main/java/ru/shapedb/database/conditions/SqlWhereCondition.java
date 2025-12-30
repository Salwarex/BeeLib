package ru.shapedb.database.conditions;

import ru.waxera.beeLib.utils.data.database.query.LogicalOperator;

public class SqlWhereCondition implements SqlCondition, PreparedCondition {

    private final LogicalOperator prevOperator;
    private final String column;
    private final Object value;

    public SqlWhereCondition(LogicalOperator prevOperator, String column, Object value){
        this.prevOperator = prevOperator;
        this.column = column;
        this.value = value;
    }

    public LogicalOperator getPrevOperator() {
        return prevOperator;
    }

    public String getColumn() {
        return column;
    }

    public Object getValue() {
        return this.value;
    }
}
