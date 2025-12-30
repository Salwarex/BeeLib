package ru.shapedb.database.statement;

import ru.shapedb.database.statement.base.SqlStatementBase;

public class Statement {
    private final SqlStatementBase base;

    public Statement(SqlStatementBase base){
        this.base = base;
    }

    public SqlStatementBase getBase() {
        return base;
    }
}
