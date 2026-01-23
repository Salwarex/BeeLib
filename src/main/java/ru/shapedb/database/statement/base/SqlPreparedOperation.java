package ru.shapedb.database.statement.base;

import ru.shapedb.database.conditions.PreparedCondition;

import java.util.List;

public interface SqlPreparedOperation {
    List<PreparedCondition> getPreparedConditions();
}
