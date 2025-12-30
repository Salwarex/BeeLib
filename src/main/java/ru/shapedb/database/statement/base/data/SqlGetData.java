package ru.shapedb.database.statement.base.data;

import ru.shapedb.database.conditions.PreparedCondition;
import ru.shapedb.database.conditions.SqlWhereCondition;
import ru.shapedb.database.statement.result.ColumnSpec;
import ru.shapedb.database.statement.base.SqlOperationQuery;
import ru.shapedb.database.statement.base.SqlPreparedOperation;
import ru.shapedb.database.statement.base.SqlStatementBase;
import ru.shapedb.database.statement.result.SqlResult;
import ru.shapedb.database.types.SqlType;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;
import java.util.stream.Collectors;


public class SqlGetData extends SqlStatementBase implements SqlPreparedOperation, SqlOperationQuery {

    private final String tableName;
    private final List<SqlWhereCondition> whereConditions;
    private final List<ColumnSpec<?>> columnSpecs = new ArrayList<>();

    public SqlGetData(String tableName,
                      SqlWhereCondition ... whereCondition){
        this.tableName = tableName;
        this.whereConditions = List.of(whereCondition);
    }

    public String getTableName() {
        return tableName;
    }

    public <T> SqlGetData column(String columnName, Class<T> clazz) {
        columnSpecs.add(new ColumnSpec<>(columnName, clazz));
        return this;
    }

    public <T> SqlGetData column(String columnName, SqlType sqlType, Class<T> clazz) {
        columnSpecs.add(new ColumnSpec<>(columnName, sqlType, clazz));
        return this;
    }

    public Set<String> getColumnsSet(){
        return columnSpecs.stream().map(cs -> cs.getColumnName()).collect(Collectors.toSet());
    }

    public List<SqlWhereCondition> getWhereConditions() {
        return whereConditions;
    }


    @Override
    public SqlResult<?> getResult() {
        return columnSpecs.isEmpty() ? null : columnSpecs.getFirst().getResult();
    }

    public List<SqlResult<?>> getResultList() {
        return columnSpecs.stream().map(ColumnSpec::getResult).collect(Collectors.toList());
    }

    @SuppressWarnings("unchecked")
    public <T> SqlResult<T> getResult(String column) {
        for (ColumnSpec<?> spec : columnSpecs) {
            if (spec.getColumnName().equals(column)) {
                return (SqlResult<T>) spec.getResult();
            }
        }
        throw new IllegalArgumentException("No column '" + column + "' requested");
    }

    @SuppressWarnings("unchecked")
    public <T> T getCastedResult(String column) {
        return (T) getResult(column).get();
    }

    @Override
    public void provideResult(ResultSet rs) {
        if (rs == null) throw new IllegalArgumentException("ResultSet is null!");
        try {
            if (rs.next()) {
                for (ColumnSpec<?> spec : columnSpecs) {
                    Object rawValue = spec.getSqlType().getValue(rs, spec.getColumnName());
                    spec.getResult().setRawValue(rawValue);
                }
            } else {
                for (ColumnSpec<?> spec : columnSpecs) {
                    spec.getResult().setRawValue(null);
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Failed to read result for query", e);
        }
    }

    @Override
    public List<PreparedCondition> getPreparedConditions() {
        return new ArrayList<>(whereConditions);
    }
}
