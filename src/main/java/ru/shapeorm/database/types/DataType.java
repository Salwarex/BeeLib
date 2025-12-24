package ru.shapeorm.database.types;

import ru.shapeorm.database.dialect.SqlExtractable;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class DataType implements SqlExtractable {
    private final SqlType type;
    private final List<Integer> parameters = new ArrayList<>();

    public DataType(SqlType type, Integer ... params){
        this.type = type;
        this.parameters.addAll(Arrays.asList(params));
    }

    public SqlType getType() {
        return type;
    }

    public List<Integer> getParameters() {
        return parameters;
    }
}
