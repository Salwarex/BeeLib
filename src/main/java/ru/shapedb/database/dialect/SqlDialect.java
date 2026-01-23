package ru.shapedb.database.dialect;

/**
 * The interface that dialect classes should implement, allowing for compatibility with different DBMS.
 * </p>
 *
 * @version 1
 * @since v1.4
 * @author Vitaliy
 */


public interface SqlDialect {
    String extract(SqlTranslatable object);
}
