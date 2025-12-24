package ru.shapeorm.dialect.sqlite;

import ru.shapeorm.database.dialect.SqlExtractable;
import ru.shapeorm.database.dialect.SqlExtractor;
import ru.shapeorm.database.types.DataType;
import ru.shapeorm.database.types.SqlType;

public class SqLiteExtractor implements SqlExtractor {

    @Override
    public String extract(SqlExtractable object) {
        if(object instanceof DataType type) return dataTypeExtraction(type);
        return "";
    }

    public String dataTypeExtraction(DataType dataType){
        SqlType type = dataType.getType();

        return switch (type){
            case CHAR, VARCHAR, NCHAR, NVARCHAR, TIME, DATE, TIMESTAMP, CLOB, INTERVAL -> "TEXT";
            case BINARY, VARBINARY, BLOB -> "BLOB";
            case DECIMAL -> "NUMERIC";
            case INTEGER, SMALLINT, BIGINT, BOOLEAN -> "INTEGER";
            case FLOAT, DOUBLE, REAL -> "REAL";
        };
    }

}
