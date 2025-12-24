package ru.shapeorm.database.conditions;

import ru.shapeorm.database.dialect.SqlExtractable;

public interface SqlCondition extends SqlExtractable {
    String getSql();
}
