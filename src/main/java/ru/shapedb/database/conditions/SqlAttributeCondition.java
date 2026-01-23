package ru.shapedb.database.conditions;

public class SqlAttributeCondition implements SqlCondition, PreparedCondition {
    private final String attributeName;
    private final Object value;
    private boolean last = false;

    public SqlAttributeCondition(String attributeName, Object value){
        this.attributeName = attributeName;
        this.value = value;
    }

    public String getAttributeName() {
        return attributeName;
    }

    public Object getValue() {
        return value;
    }

    public void last(){ this.last = true; }
}
