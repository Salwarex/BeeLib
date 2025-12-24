package ru.waxera.beeLib.utils.data.database.types;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class DataType {
    private final SqlType type;
    private final List<Integer> parameters = new ArrayList<>();

    public DataType(SqlType type, Integer ... params){
        this.type = type;
        this.parameters.addAll(Arrays.asList(params));
    }
}
