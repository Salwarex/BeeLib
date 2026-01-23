package ru.shapedb.database.statement;

import ru.shapedb.database.statement.base.SqlStatementBase;

/**
 * The executable unit of the SQL statement in the Shape module
 *
 * <p>
 * This class allows you to generate SQL queries at the Java language level,
 * which allows you to format them for each new language in parallel using {@link ru.shapedb.database.dialect.SqlDialect}.
 * One of the preset methods implementing the {@link SqlStatementBase} interface is passed to base.
 * </p>
 *
 * @version 1
 * @since v1.4
 * @author Vitaliy
 */

public class Statement {
    private final SqlStatementBase base;

    public Statement(SqlStatementBase base){
        this.base = base;
    }

    public SqlStatementBase getBase() {
        return base;
    }
}
