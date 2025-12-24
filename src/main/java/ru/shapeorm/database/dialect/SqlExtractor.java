package ru.shapeorm.database.dialect;

public interface SqlExtractor {
    String extract(SqlExtractable object);
}
