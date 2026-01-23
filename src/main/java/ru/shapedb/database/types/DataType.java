package ru.shapedb.database.types;

import ru.shapedb.database.dialect.SqlTranslatable;

import java.util.*;

public class DataType implements SqlTranslatable {
    private final SqlType type;
    private final List<Integer> parameters = new ArrayList<>();
    private final Map<DataTypeParam, String> constraints = new HashMap<>();
    private ForeignKeyRelation fkRelation;

    public DataType(SqlType type, Integer ... parameters){
        this.type = type;
        this.parameters.addAll(Arrays.asList(parameters));
        this.fkRelation = null;
    }

    public DataType constraint(DataTypeParam param, String sql){
        if(this.constraints.containsKey(param)) constraints.replace(param, sql);
        else constraints.put(param, sql);
        return this;
    }

    public Map<DataTypeParam, String> getConstraints(){
        return this.constraints;
    }

    public SqlType getType() {
        return type;
    }

    public DataType foreignKey(String otherTable, String otherColumn){
        fkRelation = new ForeignKeyRelation(otherTable, otherColumn);
        return this;
    }

    public ForeignKeyRelation getFkRelation() {
        return fkRelation;
    }

    public List<Integer> getParameters() {
        return parameters;
    }
}
