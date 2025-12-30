package ru.shapedb.database.repository;

import ru.shapedb.database.statement.Statement;

public interface DatabaseRepository {
    void execute(Statement statement);
    void executeTransaction(Statement... queries);
}
