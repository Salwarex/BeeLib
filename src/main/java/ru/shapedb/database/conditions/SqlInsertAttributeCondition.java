package ru.shapedb.database.conditions;

public class SqlInsertAttributeCondition extends SqlAttributeCondition{
    private final Object[] values;

    public SqlInsertAttributeCondition(String attributeName, Object ... values) {
        super(attributeName, null);
        if(values.length == 0) throw new IllegalArgumentException("You must specify at least one attribute!");
        this.values = values;
    }

    public int getAssociatedCount() {
        return values.length;
    }

    @Override
    public Object getValue() {
        return values[0];
    }

    public Object getValue(int row){
        return values[row];
    }
}
