package ru.waxera.beeLib.utils.data.database.query;

public class QueryWherePair {
    private final LogicalOperator prevOperator;
    private final String column;
    private final Object value;

    public QueryWherePair(LogicalOperator prevOperator, String column, Object value){
        this.prevOperator = prevOperator;
        this.column = column;
        this.value = value;
    }

    public Object getValue() {
        return this.value;
    }

    @Override
    public String toString(){
        StringBuilder result = new StringBuilder();
        result.append(prevOperator == null ? "" : prevOperator.toString() + " ")
        .append(column).append(" = ? ");
        return result.toString();
    }
}
