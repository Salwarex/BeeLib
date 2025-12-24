package ru.waxera.beeLib.utils.data.database.conditions;

import ru.waxera.beeLib.utils.data.database.query.LogicalOperator;

public class SqlWhereCondition implements SqlCondition{

    private final LogicalOperator prevOperator;
    private final String column;
    private final Object value;

    public SqlWhereCondition(LogicalOperator prevOperator, String column, Object value){
        this.prevOperator = prevOperator;
        this.column = column;
        this.value = value;
    }

    public Object getValue() {
        return this.value;
    }

    @Override
    public String getSql() {
        return ((prevOperator == null ? "" : prevOperator + " ") + column + " = ? ");
    }
}
